import json

class Card:
    def __init__(self, north, east, south, west):
        self.north = north
        self.east = east
        self.south = south
        self.west = west

class CardPos:
    def __init__(self, row, col):
        self.row = row
        self.col = col

    @property
    def in_bounds(self):
        return self.row >= 0 and self.row < 3 and self.col >= 0 and self.col < 3

    @property
    def adjacent(self):
        for new_row in [self.row - 1, self.row + 1]:
            for new_col in [self.col - 1, self.col + 1]:
                result = CardPos(new_row, new_col)
                if result.in_bounds:
                    yield result

    @property
    def linear_pos(self):
        return self.row * 3 + self.col

ALL_POSITIONS = [CardPos(row, col) for row in range(3) for col in range(3)]

class PlayedCard:
    def __init__(self, card, pos, own):
        self.card = card
        self.pos = pos
        self.own = own

    def compare(self, other):
        if self.pos.row == other.pos.row:
            if self.pos.col < other.pos.col:
                return self.card.east, other.card.west
            else:
                return self.card.west, other.card.east
        else:
            if self.pos.row < other.pos.row:
                return self.card.north, other.card.south
            else:
                return self.card.south, other.card.north

    def clone(self):
        return PlayedCard(self.card, self.pos, self.own)

def norm_comp(card1, card2):
    return card1 > card2

def json_to_card(inp):
    stren = inp['strength']
    return Card(stren['north'], stren['east'], stren['south'], stren['west'])

def read_cards():
    with open('./ttriad/resources/cards.json') as f:
        return [json.loads(line) for line in f]
