package br.cin.ufpe.epona.crawler;

import java.util.Arrays;
import java.util.HashSet;

public class CompressedExtensions {
	
	private static CompressedExtensions instance;
	private HashSet<String> extensions = new HashSet<String>();
	
	public static CompressedExtensions getInstance() {
		if (instance == null) {
			instance = new CompressedExtensions();
		}
		return instance;
	}
	
	// from: http://en.wikipedia.org/wiki/List_of_archive_formats
	private CompressedExtensions() {
		String[] extensionsStr =  { ".a",
									".ar",
									".cpio",
									".shar",
									".LBR",
									".iso",
									".lbr",
									".mar",
									".tar",
									".bz2",
									".F",
									".gz",
									".lz",
									".lzma",
									".lzo",
									".rz",
									".sfark",
									".?Q?",
									".?Z?",
									".xz",
									".z",
									".Z",
									".infl",
									".??_",
									".7z",
									".s7z",
									".ace",
									".afa",
									".alz",
									".apk",
									".arc",
									".arj",
									".ba",
									".bh",
									".cab",
									".cfs",
									".cpt",
									".dar",
									".dd",
									".dgc",
									".dmg",
									".gca",
									".ha",
									".hki",
									".ice",
									".j",
									".kgb",
									".lzh",
									".lha",
									".lzx",
									".pak",
									".partimg",
									".paq6",
									".paq7",
									".paq8",
									".pea",
									".pim",
									".pit",
									".qda",
									".rar",
									".rk",
									".sda",
									".sea",
									".sen",
									".sfx",
									".sit",
									".sitx",
									".sqx",
									".tar.gz",
									".tgz",
									".tar.Z",
									".tar.bz2",
									".tbz2",
									".tar.lzma",
									".tlz",
									".uc",
									".uc0",
									".uc2",
									".ucn",
									".ur2",
									".ue2",
									".uca",
									".uha",
									".wim",
									".xar",
									".xp3",
									".yz1",
									".zip",
									".zipx",
									".zoo",
									".zz"};
		extensions.addAll(Arrays.asList(extensionsStr)); 
	}
	
	public boolean contains(String extension) {
		return extensions.contains(extension);
	}
	
}
