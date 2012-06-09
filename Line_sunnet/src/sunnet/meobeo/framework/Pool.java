package sunnet.meobeo.framework;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
	public interface PoolObjectFactory<T> {
		public T createObject();
	}

	private final List<T> freeObjects; // dung de luu tru object
	private final PoolObjectFactory<T> factory; // dung de tao object moi
	private final int maxSize; // kich thuoc lon nhat co the luu tru

	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}

	public T newObject() {
		T object = null;
		if (freeObjects.size() == 0)
			object = factory.createObject(); // neu chua co object thi tao
												// object moi
		else
			object = freeObjects.remove(freeObjects.size() - 1); // lưu phần tử
																	// cuối cùng
																	// trong
																	// list
		return object;
	}

	public void free(T object) {
		if (freeObjects.size() < maxSize)
			freeObjects.add(object);
	}
}
