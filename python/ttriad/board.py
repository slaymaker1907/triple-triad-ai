from ttriad.card import CardPos, norm_comp, ALL_POSITIONS
from collections import Counter

class Board:
    def __init__(self):
        self.places = [None] * 9

    def __getitem__(self, pos):
        return self.places[pos.linear_pos]

    def __setitem__(self, pos, card):
        self.places[pos.linear_pos] = card

    def is_empty(self, pos):
        return self[pos] is None

    @property
    def empty_pos(self):
        for row in range(3):
            for col in range(3):
                result = CardPos(row, col)
                if self.is_empty(result):
                    yield result

    def winner(self, snd):
        counter = Counter(map(lambda card: card.own, self.places))
        counter[snd] += 1
        pos = counter[1]
        neg = counter[-1]
        if pos > neg:
            return 1
        elif pos < neg:
            return -1
        else:
            return 0

    def clone(self):
        def clone_card(cd):
            if cd is None:
                return None
            else:
                return cd.clone()
        result = Board()
        for pos in ALL_POSITIONS:
            result[pos] = clone_card(self[pos])
        return result

def play_card(board, card):
    pos = card.pos
    board[pos] = card
    for adj in pos.adjacent:
        if not board.is_empty(adj):
            other = board[adj]
            if card.own != other.own:
                atr1, atr2 = card.compare(other)
                if norm_comp(atr1, atr2):
                    other.own = card.own
