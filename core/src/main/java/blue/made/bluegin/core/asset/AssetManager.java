package blue.made.bluegin.core.asset;

import java.nio.file.Path;

public interface AssetManager {
	DataSourceAsset findAssetData(Path asset);

	/** Add the given asset to the cache and return it if it is not present, otherwise return the cached asset.
	 *
	 * @param asset The asset to cache or find in the cache
	 * @return The cached asset
	 */
	CachableAsset intern(CachableAsset asset);
}
