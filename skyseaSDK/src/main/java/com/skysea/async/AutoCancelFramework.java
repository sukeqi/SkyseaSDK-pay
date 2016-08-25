package com.skysea.async;

import com.skysea.app.BaseActivity;


public abstract class AutoCancelFramework<Params, Progress, Result> extends
		AsyncFramework<Params, Progress, Result> {
	
	AutoCancelFramework(BaseActivity activity){
		super();
		mAutoCancelController = activity.getAutoCancelController();
	}
	
	AutoCancelFramework(AutoCancelController controller){
		super();
		mAutoCancelController = controller;
	}
	
	@Override
	protected void finish(Result result) {
		if(mAutoCancelController != null) {
			mAutoCancelController.remove(this);
			mAutoCancelController = null;
		}
		super.finish(result);
	}
	
	protected AutoCancelController mAutoCancelController;
}
