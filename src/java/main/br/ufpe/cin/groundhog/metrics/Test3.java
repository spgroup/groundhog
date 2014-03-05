package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;

import br.ufpe.cin.groundhog.extractor.GitCommitExtractor;

public class Test3 {
	
	enum lol {
		
	}
	
	public void zoeira1(String k, String i, String j){
		System.out.println("HAHAHAHAH");
		System.out.println("HAHAHAHAH");
		System.out.println("HAHAHAHAH");
		System.out.println("HAHAHAHAH");
		System.out.println("HAHAHAHAH");
	}
	
	public void zoeira(String ki, String i){
		try {
			int k = 0;
			synchronized (this) {
				do{
					System.out.println("Testando, testando, testando, tetetetetestando!");
					
					ArrayList<Integer> arr = new ArrayList<Integer>(){
						
						@Override
						public String toString() {
							
							int nnumber = 10;
							
							while(nnumber != 0){
								
								try {
									
									super.toString();
									nnumber--;
								} catch (ArrayIndexOutOfBoundsException e){
									if(nnumber == 0){
										System.out.println("Zoeira never ends");
									}else{
										System.out.println("kkkkkk");
									}
								} catch (NullPointerException e){
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							return super.toString();
							
						}
					};
				}while(k++<10);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");
		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
//		System.out.println("Testando, testando, testando, tetetetetestando!");
	}
	
	public void zoeira2(String k, String i,String ki, String ii){
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
