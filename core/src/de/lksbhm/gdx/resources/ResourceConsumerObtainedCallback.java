package de.lksbhm.gdx.resources;

public interface ResourceConsumerObtainedCallback<T extends ResourceConsumer> {
	void onObtained(T consumer);
}
