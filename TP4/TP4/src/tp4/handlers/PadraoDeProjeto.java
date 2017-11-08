package tp4.handlers;



public class PadraoDeProjeto {
	
	public void escreverFabrica(Enumerado param) {
		System.out.println("public enum "+param.getNomeMetodo()+" {");
		String codigoEnum = "\t";
		String nomeMetodo= param.getNomeMetodo()+" obter"+param.getNomeMetodo()+"()";
		String codigoEnumAbstract = "\tpublic abstract Enum"+nomeMetodo+";\n";
		
		String prov = "";
			for(int i = 0; i < param.getNomeEnum().size();i++) {
				prov = param.getNomeEnum().get(i).toString();
				codigoEnum+= prov;
				codigoEnum+="{\n\t\t@Override\n\t\tpublic ";
				codigoEnum+=nomeMetodo;
				codigoEnum+="{\r\n" + 
						"\t\t\treturn new ";
				codigoEnum+=prov;
				codigoEnum+="();\r\n" + 
						"\t		}\r\n" + 
						"\t	}\n\t";
			}
			System.out.println(codigoEnum);
			System.out.println(codigoEnumAbstract+"\n}");
	}
	


	public void escreverClasses(Enumerado param, Classes classe) {
		String a;
		String metodo = "";
		for(int i = 0; i < param.getNomeEnum().size();i++) {
			System.out.println("public class "+param.getNomeEnum().get(i)+" implements "+param.getNomeMetodo()+" {\n\t");
		}
//		metodo += "public ";
//  	metodo+= intPegar;
//		metodo+= " ";
//		metodo+= classe.getMetodo();
//		metodo+="(";
//		metodo+=classe.getParametros().get(i);
//		metodo+=") {";
//	System.out.println(metodo);
		
		 
		
	}
	
}
