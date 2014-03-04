package br.ufpe.cin.groundhog.metrics;

import java.io.File;

import br.ufpe.cin.groundhog.extractor.GitCommitExtractor;

public class Test3 {
	public void zoeira(){
		try {
			int k = 0;
			do{
				System.out.println("Testando, testando, testando, tetetetetestando!");
			}while(k++<10);
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");
	}
	
	public void zoeira2(){
		if(true){
			if(true){
				System.out.println("Testando, testando, testando, tetetetetestando!");
			}
		}
	}
	
	public static void main(String[] args) {
		GitCommitExtractor extractor = new GitCommitExtractor();
		File file = new File("/home/bruno/scm/github.com/learnhaskell");
		if(file.isDirectory()){
			extractor.extractCommits(file);
			extractor.extractCommits(file);
		}
		
	}
}
