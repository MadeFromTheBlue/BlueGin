package blue.made.bluegin.asset;

/**
 * This asset can be cached by the AssetManager. It is responsible for storing the cached data.
 *
 * @param <T>
 */
public interface CachableAsset<T> extends Asset<T> {

    public default String Name() {
        return String.valueOf(getIdentifier());
    }

    public Object getIdentifier();

    public default <O extends CachableAsset<T>> O intern(AssetManager manager) {
        return (O) manager.intern(this);
    }
}
