package com.mftbgames.bluegin.util;

import java.util.Deque;
import java.util.LinkedList;

/**
 * <i>RunQueue</i> is a utility class for the ordered execution of
 * {@link java.lang.Runnable Runnables} using many threads. Each instance of
 * RunQueue maintains a number of slots, each corresponding to a thread. The
 * number of slots can be changed at any time using {@link #setSlots(int)
 * setSlots}. When a slot finishes, it will run the next item in the queue. If
 * the queue is empty then the corresponding thread dies until new runnables are
 * added. RunQueue guarantees that runnables will be executed in order, but does
 * not, of course, guarantee that they will finish in order.
 */
public class RunQueue
{
	private static int nextQueueThread = 0;
	
	/**
	 * A wrapper class for a {@link java.lang.Thread Thread} that manages the
	 * execution of a slot.
	 */
	public class QueueThread
	{
		private Thread thread;
		private Runnable target;
		
		private QueueThread()
		{
			this.thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					QueueThread.this.run();
				}
			}, "QueueThread-" + nextQueueThread++);
		}
		
		private void run()
		{
			while (true)
			{
				this.target.run();
				synchronized (RunQueue.this)
				{
					if (RunQueue.this.active.size() <= RunQueue.this.slotCount && !RunQueue.this.queue.isEmpty())
					{
						this.target = RunQueue.this.queue.poll();
					}
					else
					{
						RunQueue.this.active.remove(this);
						RunQueue.this.avalible.addFirst(this);
						return;
					}
				}
			}
		}
		
		public void start(Runnable target)
		{
			if (target == null)
			{
				throw new IllegalArgumentException("target cannot be null");
			}
			this.target = target;
			this.thread.start();
		}
	}
	
	private int slotCount = 0;
	private LinkedList<QueueThread> avalible = new LinkedList<>();
	private LinkedList<QueueThread> active = new LinkedList<>();
	private Deque<Runnable> queue = new LinkedList<>();
	
	/**
	 * Creates a new RunQueue with an initial set of slots to use. The threads
	 * used by these slots will be parented to whatever thread called this
	 * constructor.
	 * 
	 * @see #setSlots(int)
	 * @param slots The initial number of slots
	 */
	public RunQueue(int slots)
	{
		this.setSlots(slots);
	}
	
	public synchronized Runnable getOldest()
	{
		if (this.active.isEmpty())
		{
			return null;
		}
		return this.active.peekFirst().target;
	}
	
	public synchronized Runnable getNewest()
	{
		if (this.active.isEmpty())
		{
			return null;
		}
		return this.active.peekLast().target;
	}
	
	public synchronized Runnable[] getRunning()
	{
		Runnable[] slots = new Runnable[this.active.size()];
		int i = 0;
		for (QueueThread slot : this.active)
		{
			slots[i] = slot.target;
			i++;
		}
		return slots;
	}
	
	/**
	 * Adds a new runnable to the end of the queue
	 * 
	 * @param target The runnable to add
	 */
	public synchronized void add(Runnable target)
	{
		if (this.active.size() < this.slotCount)
		{
			this.run(target);
		}
		else
		{
			this.queue.add(target);
		}
	}
	
	/**
	 * Adds a new runnable to the =front of the queue
	 * 
	 * @param target The runnable to add
	 */
	public synchronized void addFirst(Runnable target)
	{
		if (this.active.size() < this.slotCount)
		{
			this.run(target);
		}
		else
		{
			this.queue.addFirst(target);
		}
	}
	
	/**
	 * Gets the number of runnables currently waiting to be executed
	 * 
	 * @return The queue length
	 */
	public synchronized int queueLength()
	{
		return this.queue.size();
	}
	
	/**
	 * Clears the queue of runnables
	 */
	public synchronized void clearQueue()
	{
		this.queue.clear();
	}
	
	/**
	 * The capacity of a RunQueue is the number of threads it has previously
	 * created. If the target number of slots is increased to above this number,
	 * then new threads must be constructed. Keep in mind that this is NOT the
	 * number of active slots or the target number of slots.
	 * 
	 * @return The capacity of this RunQueue
	 */
	public synchronized int capacity()
	{
		return this.active.size() + this.avalible.size();
	}
	
	/**
	 * Gets the number of slots that are currently active. The returned number
	 * should almost always equal the result of {@link #slots() slots()}. This
	 * might not be the case if the queue is empty or if the target number of
	 * slots was recently decreased by {@link #setSlots(int) setSlots()}.
	 * 
	 * @return The current number of active slots
	 */
	public synchronized int inUse()
	{
		return this.active.size();
	}
	
	/**
	 * Gets the current target number of slots (as set by {@link #setSlots(int)
	 * setSlots()}). The actual number of active slots (as given by
	 * {@link #inUse() inUse()}) might differ from this if the queue is empty or
	 * if the target number of slots was recently decreased.
	 * 
	 * @return The target number of slots
	 */
	public synchronized int slots()
	{
		return this.slotCount;
	}
	
	/**
	 * Changes the number of slots/threads this RunQueue is to use. If number is
	 * decreased, all currently active slots will be allowed to finish but
	 * runnables from the queue will not be executed until the number of active
	 * slots has fallen sufficiently. If the number is increased, previously
	 * removed threads (from a past call to {@link #setSlots(int) setSlots}) are
	 * reused before any new threads are created. Any new threads will be
	 * parented to the thread that called this method.
	 * 
	 * @param slots The target number of slots
	 */
	public synchronized void setSlots(int slots)
	{
		this.slotCount = slots;
		int toMake = this.slotCount - this.capacity();
		for (int i = 0; i < toMake; i++)
		{
			this.avalible.add(new QueueThread());
		}
		this.fillSlots();
	}
	
	/**
	 * 
	 */
	protected synchronized void fillSlots()
	{
		while (!this.queue.isEmpty() && this.active.size() < this.slotCount)
		{
			this.run(this.queue.poll());
		}
	}
	
	private synchronized void run(Runnable target)
	{
		if (target != null)
		{
			QueueThread thread = this.avalible.poll();
			this.active.add(thread);
			thread.start(target);
		}
	}
}
