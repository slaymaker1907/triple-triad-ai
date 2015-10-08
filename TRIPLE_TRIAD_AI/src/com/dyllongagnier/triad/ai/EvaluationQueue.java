package com.dyllongagnier.triad.ai;

import java.util.concurrent.PriorityBlockingQueue;

public class EvaluationQueue
{
	private static EvaluationWorker[] workers = getNewPool(Runtime.getRuntime().availableProcessors());
	private final static PriorityBlockingQueue<DualRunner> priorityQueue = new PriorityBlockingQueue<>();
	private static int maxQueueSize = Integer.MAX_VALUE;
	
	public static void setThreadCount(int threads)
	{
		if (workers.length != threads)
		{
			for(EvaluationWorker worker : workers)
				worker.destroy();
			workers = getNewPool(threads);
		}
	}
	
	/**
	 * This method destroys the queue such that setThreadCount must be called to do work again.
	 */
	public static void stopAllThreads()
	{
		for(int i = 0; i < workers.length; i++)
			workers[i].destroy();
	}
	
	private static EvaluationWorker[] getNewPool(int threads)
	{
		EvaluationWorker[] result = new EvaluationWorker[threads];
		for(int i = 0; i < threads; i++)
		{
			result[i] = new EvaluationWorker(EvaluationQueue::doNothingSupplier, EvaluationQueue::doNothingSupplier);
			result[i].start();
		}
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
			worker.finishQuickly = true;
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
	
	public static void addNodeToQueue(DualRunner runner)
	{
		priorityQueue.add(runner);
		if (priorityQueue.size() > EvaluationQueue.maxQueueSize)
			EvaluationQueue.finishProcessingQuickly();
	}
	
	private static void processNode()
	{
		DualRunner toProcess = priorityQueue.poll();
		if (toProcess != null)
			toProcess.slowRun.run();
	}
	
	private static void processNodeQuickly()
	{
		DualRunner toProcess = priorityQueue.poll();
		if (toProcess != null)
			toProcess.fastRun.run();
	}
	
	public static void setMaxQueueSize(int size)
	{
		EvaluationQueue.maxQueueSize = size;
	}
}
