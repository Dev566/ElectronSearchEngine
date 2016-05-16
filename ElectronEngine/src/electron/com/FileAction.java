package electron.com;

import java.io.File;

public class FileAction {
	private String FromFile;
	private String ToFile;

	FileAction(String InputFile) {
		this.FromFile = InputFile;
	}

	FileAction(String InputFile, String OutputFile) {
		this.FromFile = InputFile;
		this.ToFile = OutputFile;
	}

	public void MoveDirectoryContent() {
		File DirectoryTOClear = new File(FromFile);
		File[] AllFiles = DirectoryTOClear.listFiles();

		for (int i = 0; i < AllFiles.length; i++) {
			String FileName = AllFiles[i].getName();
			File NewFilePath = new File(ToFile + FileName);
			if (AllFiles[i].renameTo(NewFilePath)) {
				System.out.println(FileName + " is moved successful!");
			}
		}

	}

//	public static void main(String[] args) {
//		FileAction FileDirectory = new FileAction("F:/IR/OLD/", "F:/IR/New/");
//		FileDirectory.MoveDirectoryContent();
//
//	}

}
