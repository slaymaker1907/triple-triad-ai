package com.dyllongagnier.triad.ai;

import java.util.concurrent.PriorityBlockingQueue;

public class EvaluationQueue
{
	private static EvaluationWorker[] workers = getNewPool(Runtime.getRuntime().availableProcessors());
	private final static PriorityBlockingQueue<BoardNode> priorityQueue = new PriorityBlockingQueue<BoardNode>();
	
	public static void setThreadCount(int threads)
	{
		if (workers.length != threads)
		{
			for(EvaluationWorker worker : workers)
				worker.destroy();
			workers = getNewPool(threads);
		}
	}
	
	private static EvaluationWorker[] getNewPool(int threads)
	{
		EvaluationWorker[] result = new EvaluationWorker[threads];
		for(int i = 0; i < threads; i++)
			result[i] = new EvaluationWorker(EvaluationQueue::doNothingSupplier, EvaluationQueue::doNothingSupplier);
		return result;
	}
	
	private static Runnable doNothingSupplier()
	{
		return () -> 
		{
		};
	}
	
	public static void beginProcessing()
	{
		for(EvaluationWorker worker : workers)
		{
			worker.finishQuickly = false;
			worker.getMainWork = () -> EvaluationQueue::processNode;
			worker.getQuickWork = () -> EvaluationQueue::processNodeQuickly;
		}
	}
	
	public static void finishProcessingQuickly()
	{
		for(EvaluationWorker worker : workers)
		{
			worker.finishQuickly = false;
		}
	}
	
	public static void stopProcessing()
	{
		for(EvaluationWorker worker : workers)
		{
			worker.finishQuickly = false;
			worker.getMainWork = EvaluationQueue::doNothingSupplier;
			worker.getQuickWork = EvaluationQueue::doNothingSupplier;
		}
	}
	
	public static boolean workComplete()
	{
		return priorityQueue.isEmpty();
	}
	
	public static void addNodeToQueue(BoardNode node)
	{
		priorityQueue.add(node);
	}
	
	private static void processNode()
	{
		BoardNode toProcess = priorityQueue.poll();
		toProcess.regularEvaluation();
	}
	
	private static void processNodeQuickly()
	{
		BoardNode toProcess = priorityQueue.poll();
		toProcess.immediateEvaluation();
	}
}
