package com.huoqiu.framework.imageloader.core.listener;

/**
 * A convenient class to extend when you only want to listen for a subset of all the image loading events. This
 * implements all methods in the {@link ImageLoadingListener} but does
 * nothing.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.4.0
 */
public class SimpleImageLoadingListener implements ImageLoadingListener {

	@Override
	public void onLoadingStarted() {

	}

	@Override
	public void onLoadingFailed() {

	}

	@Override
	public void onLoadingSuccess() {

	}

	@Override
	public void onLoadingCancelled() {

	}

	@Override
	public void onLoadingFinish() {

	}
}
