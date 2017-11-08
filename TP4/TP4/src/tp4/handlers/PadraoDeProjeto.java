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
	
	public void escreverClasses(Enumerado param) {
		System.out.println("public enum "+param.getNomeMetodo()+" {");
	}
}
