from lxml import etree
import json
import re
import sys

def main():
    img_reg = re.compile('assets/images/cardinfo/([\dA]).png')
    html = etree.parse(sys.stdin, etree.HTMLParser())
    cards = html.xpath('//div[@class="card ng-scope"]')
    for card in cards:
        name = ''.join(card.xpath('span[@class="card-name"]/descendant-or-self::*/text()')).strip()
        def get_power(pos):
            result = card.xpath('span[@class="card-number"]/img[@class="card-{0}"]/@src'.format(pos))[0]
            parsed = img_reg.match(result).group(1)
            if 'A' in parsed:
                return 10
            else:
                return int(parsed)
        north = get_power('top')
        east = get_power('left')
        south = get_power('bottom')
        west = get_power('right')
        result = {
            'name': name,
            'strength': {
                'north': north,
                'east': east,
                'south': south,
                'west': west
            }
        }
        print(json.dumps(result))

if __name__ == '__main__':
    main()
