package io.phalanx.Logic.Utility.Multithreading;
import io.phalanx.Logic.Scraping.HTML;
import io.phalanx.Logic.Utility.Processor;

import java.util.concurrent.Callable;

public class ProcessorTask implements Callable<Boolean> {

	private HTML html;
	
	public ProcessorTask(HTML html) {
		this.html = html;
	}

	@Override
	public Boolean call() throws Exception {
		Processor.download(html);
		
		return true;
	}

}
