class EvaluationQueue:
	def __init__(self):
		self.size = 0
		self.max_size = 0
		
	def add_node(self, num):
		self.size += num
		if self.size > self.max_size:
			self.max_size = self.size
		
	def pop_node(self):
		self.size -= 1
		
def ai(queue, hand1, hand2):
	board_count = 10 - hand1 - hand2
	if board_count == 9:
		return
	queue.pop_node()
	iters = 0
	if hand1 == hand2:
		iters = hand1 * (9 - board_count)
		hand1 = hand1 - 1
	else:
		iters = hand2 * (9 - board_count)
		hand2 = hand2 - 1
	queue.add_node(iters)
	for i in range(iters):
		ai(queue, hand1, hand2)

# (4, 5) = 116,121,599
def calc_queue_size(hand1, hand2):
	queue = EvaluationQueue()
	ai(queue, hand1, hand2)
	return queue.max_size