package com.skysea.async;

import java.util.concurrent.CancellationException;

import com.skysea.app.BaseActivity;
import com.skysea.service.IPlatService;
import com.skysea.utils.ServiceFactory;

public abstract class AutoCancelServiceFramework<Params, Progress, Result>
		extends AutoCancelFramework<Params, Progress, Result> {
	
	protected IPlatService mIPlatService;
	
	public AutoCancelServiceFramework(AutoCancelController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	public AutoCancelServiceFramework(BaseActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	protected synchronized void createIPlatCokeService()
			throws CancellationException {
		if (isCanceled()) {
			throw new CancellationException();
		}
		if (mIPlatService != null) {
			return;
		}
		//return PlatServiceAgent obj
		mIPlatService = ServiceFactory.get().createPlatService();
	}


	@Override
	protected void finish(Result result) {
		// TODO Auto-generated method stub
		super.finish(result);
		releaseAllServices();
	}

	private void releaseAllServices() {
		synchronized (this) {
			if (mIPlatService != null) {
				mIPlatService = null;
			}
		}
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
		synchronized (this) {
			if (mIPlatService != null) {
				mIPlatService.abortService();
			}
		}
	}


	@Override
	protected Result doInBackground(Params... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
