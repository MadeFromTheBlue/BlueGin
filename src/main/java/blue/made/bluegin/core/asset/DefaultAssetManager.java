package blue.made.bluegin.core.asset;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultAssetManager implements AssetManager {
	public ArrayList<Path> assetSearchPaths = new ArrayList<>();
	public HashMap<Object, CachableAsset> cache = new HashMap<>();

	@Override
	public DataSourceAsset findAssetData(Path asset) {
		for (Path path : assetSearchPaths) {
			File full = path.resolve(asset).toFile();
			if (full.exists()) {
				return new FileAsset(full).getDataAsset();
			}
		}
		return null;
	}

	@Override
	public CachableAsset intern(CachableAsset asset) {
		Object id = asset.getIdentifier();
		CachableAsset out = cache.get(id);
		if (out == null) {
			cache.put(id, asset);
			return asset;
		}
		return out;
	}

	public void deintern(CachableAsset asset) {
		cache.remove(asset.getIdentifier());
	}

	public void addSearchPath(Path path) {
		this.assetSearchPaths.add(path);
	}
}
