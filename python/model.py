from ttriad import card, board
import random
from sklearn.ensemble import RandomForestRegressor, RandomForestClassifier
from collections import Counter
import json
from itertools import chain
import numpy as np
import pickle

FILENAME = 'database.json'
PLAYER_FILE = 'players.json'
DECK_MODEL = 'decks.pickle'

class RandomPlayer:
    def __init__(self, cards, num):
        self.cards = list(cards)
        self.num = num

    def play(self, bd):
        to_play = random.choice(self.cards)
        self.cards.remove(to_play)
        pos = random.choice(list(bd.empty_pos))
        to_play = card.PlayedCard(to_play, pos, self.num)
        board.play_card(bd, to_play)

# [current_player, cards_in_hand1, cards_in_hand-1, [empty, owner, north, east, south, west for each]]
def record_play(hand1, hand2, bd, lib, current):
    board_json = [[None]*3]*3
    for pos in card.ALL_POSITIONS:
        car = bd[pos]
        if car is not None:
            car_j = {
                'owner': car.own,
                'card': lib.reverse_lookup(car.card)
            }
        else:
            car_j = None
        board_json[pos.row][pos.col] = car_j
    def ser_hand(hand):
        return [lib.reverse_lookup(cd) for cd in hand]
    hands = [
        {
            'player': 1,
            'hand': ser_hand(hand1)
        },
        {
            'player': -1,
            'hand': ser_hand(hand2)
        }
    ]
    return {
        'current_player': current,
        'hands': hands,
        'board': board_json
    }


class Library:
    def __init__(self):
        self.card_list = card.read_cards()
        self.cards = [card.json_to_card(inp) for inp in self.card_list]
        self.cardids = {card:i for i, card in enumerate(self.cards)}

    def random_inds(self):
        return random.sample(range(len(self.cards)), 5)

    def random_deck(self):
        return self.assemble_deck(self.random_inds())

    def assemble_deck(self, indexes):
        return [self.cards[pos] for pos in indexes]

    def make_sparse(self, indexes):
        result = [0] * len(self.cards)
        for i in indexes:
            result[i] = 1
        return result

    def to_names(self, indexes):
        return [self.card_list[i]['name'] for i in indexes]

    def reverse_lookup(self, card):
        return self.cardids[card]

def play_random(deck1, deck2):
    bd = board.Board()
    players = {1: RandomPlayer(deck1, 1), -1: RandomPlayer(deck2, -1)}
    current_player = random.choice([1, -1])
    for i in range(9):
        players[current_player].play(bd)
        current_player *= -1
    return bd.winner(current_player)

def evaluate(deck1, deck2, games=10):
    return Counter(play_random(deck1, deck2) for i in range(games))

def play_game(players, lib):
    bd = board.Board()
    current_player = random.choice([1, -1])
    plays = [record_play(players[1].cards, players[-1].cards, bd, lib, current_player)]
    for i in range(9):
        players[current_player].play(bd)
        current_player *= -1
        plays.append(record_play(players[1].cards, players[-1].cards, bd, lib, current_player))
    return {
        'winner': bd.winner(current_player),
        'plays': plays
    }

def collect_player_data(sample_size=100000):
    lib = Library()
    with open(PLAYER_FILE, 'a') as f:
        for i in range(sample_size):
            players = {1:RandomPlayer(lib.random_deck(), 1), -1:RandomPlayer(lib.random_deck(), -1)}
            print(json.dumps(play_game(players, lib)), file=f)

def collect_data(samp_size=10000):
    lib = Library()
    with open(FILENAME, 'a') as f:
        for i in range(samp_size):
            deck1ind, deck2ind = lib.random_inds(), lib.random_inds()
            deck1, deck2 = lib.assemble_deck(deck1ind), lib.assemble_deck(deck2ind)
            results = evaluate(deck1, deck2)
            def to_json(deck, wins):
                return {
                    'deck': deck,
                    'wins': wins
                }
            to_write = [to_json(deck1ind, results[1]), to_json(deck2ind, results[-1])]
            print(json.dumps(to_write), file=f)

def read_db(lib):
    with open(FILENAME) as f:
        result = list(chain.from_iterable(json.loads(line) for line in f))
    return [lib.make_sparse(deck['deck']) for deck in result], [deck['wins'] for deck in result]

def train(lib):
    model = RandomForestRegressor(n_jobs=1)
    data, outputs = read_db(lib)
    model.fit(data, outputs)
    return model

def get_deck(model, lib, dcount=20):
    decks = [lib.random_inds() for i in range(dcount)]
    predicted = model.predict([lib.make_sparse(deck) for deck in decks])
    result = max(range(dcount), key=lambda i: predicted[i])
    return decks[result]

def eval_model(model, lib, games=10000):
    result = Counter()
    #mod_deck = get_deck(model, lib, dcount=1000000)
    #print(lib.to_names(mod_deck))
    #mod_deck = lib.assemble_deck(mod_deck)
    for i in range(games):
        mod_deck = lib.assemble_deck(get_deck(model, lib, dcount=20))
        rand_deck = lib.random_deck()
        result.update(evaluate(mod_deck, rand_deck))
    return result[1] / (result[-1] + result[1])

def flatten_list(*lst):
    return list(chain.from_iterable(lst))

def read_players(lib):
    def parse_json(parsed):
        decks = parsed['hands']
        deck1 = decks[1]['hand']
        deck2 = decks[-1]['hand']
        current = parsed['current_player']
        bd_data = parsed['board']
        bd = board.Board()
        for pos in card.ALL_POSITIONS:
            card_json = bd_data[pos.row][pos.col]
            if card_json is not None:
                car = card.PlayedCard(lib.cards[card_json['card']], pos, card_json['owner'])
            else:
                car = None
            bd[pos] = car
        return map_gamestate(deck1, deck2, bd, current, lib)
    plays = []
    outcomes = []
    with open(PLAYER_FILE) as f:
        for line in f:
            parsed = json.loads(line)
            current_plays = parsed['plays']
            outcomes += [parsed['winner']] * len(current_plays)
            plays += [parse_json(play) for play in current_plays]
    return np.array(plays), np.array(outcomes)

def train_player(lib):
    model = RandomForestClassifier(n_jobs=1)
    data, outputs = read_players(lib)
    model.fit(data, outputs)
    print('Model is trained.')
    return model

def eval_player(player_model, lib, deck_model, games=10000):
    result = Counter()
    for i in range(games):
        decks1 = lib.assemble_deck(get_deck(deck_model, lib, dcount=20))
        #decks1 = lib.random_deck()
        decks2 = lib.random_deck()
        player2 = RandomPlayer(decks2, -1)
        #player1 = RandomPlayer(decks1, 1)
        #player1 = RandomPlayer(decks1, 1)
        player1 = GoodPlayer(decks1, 1, player_model, player2.cards, lib)
        players = {1: player1, -1: player2}
        result[play_game(players, lib)['winner']] += 1
        print(result[1] / (result[1] + result[-1]))
    return result[1] / (result[1] + result[-1])

def map_gamestate(deck1, deck2, bd, current, lib):
    deck1 = lib.make_sparse(deck1)
    deck2 = lib.make_sparse(deck2)
    bd_vec = [[0, 0, 0, 0, 0, 0]]*9
    for pos in card.ALL_POSITIONS:
        car = bd[pos]
        if car is not None:
            row = bd_vec[pos.linear_pos]
            row[0] = 1 # Is present
            row[1] = car.own
            stren = car.card
            row[2] = stren.north
            row[3] = stren.east
            row[4] = stren.south
            row[5] = stren.west
    bd_vec = flatten_list(*bd_vec)
    return np.array(flatten_list([current], deck1, deck2, bd_vec))

class GoodPlayer:
    def __init__(self, cards, num, model, other_hand, lib):
        self.cards = set(cards)
        self.num = num
        self.model = model
        self.other_hand = other_hand
        self.lib = lib

    def play(self, bd):
        def prob_loss(bd):
            if self.num == 1:
                deck1 = self.cards
                deck2 = self.other_hand
            else:
                deck2 = self.cards
                deck1 = self.other_hand
            deck1 = [self.lib.reverse_lookup(cd) for cd in deck1]
            deck2 = [self.lib.reverse_lookup(cd) for cd in deck2]
            state = map_gamestate(deck1, deck2, bd, self.num, self.lib)
            result = self.model.predict_proba([state])[0]
            index = {num:i for i, num in enumerate(self.model.classes_)}[-1 * self.num] # Doing minimax.
            return result[index]
        results = dict()
        for cd in self.cards:
            for pos in bd.empty_pos:
                to_play = card.PlayedCard(cd, pos, self.num)
                copy = bd.clone()
                board.play_card(copy, to_play)
                results[to_play] = prob_loss(copy)
        to_play = min(results.keys(), key=results.get)
        self.cards.remove(to_play.card)
        board.play_card(bd, to_play)

if __name__ == '__main__':
    #collect_data(100000)
    #collect_player_data()
    lib = Library()
    PMODEL_FILE = 'player_model.pickle'
    '''
    model = train_player(lib)
    with open(PMODEL_FILE, 'wb') as f:
        pickle.dump(model, f, pickle.HIGHEST_PROTOCOL)
    deck_model = train(lib)
    with open(DECK_MODEL, 'wb') as f:
        pickle.dump(deck_model, f, pickle.HIGHEST_PROTOCOL)
    '''
    with open(DECK_MODEL, 'rb') as f:
        deck_model = pickle.load(f)
    with open(PMODEL_FILE, 'rb') as f:
        model = pickle.load(f)
    print(eval_player(model, lib, deck_model))
    #lib = Library()
    #model = train(lib)
    #print(eval_model(model, lib))
