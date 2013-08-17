package io.phalanx.Logic;
import io.phalanx.Logic.Scraping.HTML.HTML;

import java.util.concurrent.Callable;

public class ProcessorTask implements Callable<Boolean> {

	private HTML html;
	
	public ProcessorTask(HTML html) {
		this.html = html;
	}

	@Override
	public Boolean call() throws Exception {
		Processor processor = new Processor();
		processor.download(html);
		
		return true;
	}

}
