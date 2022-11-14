package com.example.tanzu.atm;

public abstract class BaseTestPlan
{	
	public void perform() throws Exception 
	{
		try 
		{
			setupMocks();
			Exception exception = null;
			try 
			{
				performInner();
			} 
			catch (Exception e) 
			{
				exception = e;
			}
			assertException(exception);
		} 
		finally 
		{
			tearDown();
		}
	}

	protected abstract void performInner() throws Exception;

	protected void setupMocks() {
	}

	protected void tearDown() {
	}

	protected void assertException(Exception exception) throws Exception {
		// default case should not throw an exception
		if (exception != null) {
			throw exception;
		}
	}
}
