package blue.made.bluegin.core.asset;

import java.io.*;

public class FileAsset implements Asset<File> {
	private File file;
	private String name;

	public FileAsset(File file) {
		this.file = file;
		name = file.getAbsolutePath();
	}

	@Override
	public File getData() {
		return file;
	}

	public DataSourceAsset getDataAsset() {
		return new DataSourceAsset() {
			@Override
			public InputStream getData() {
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					return null;
				}
			}

			@Override
			public String getName() {
				return FileAsset.this.getName();
			}
		};
	}

	@Override
	public String getName() {
		return name;
	}
}
