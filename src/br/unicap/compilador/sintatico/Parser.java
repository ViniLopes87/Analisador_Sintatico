package br.unicap.compilador.sintatico;

import br.unicap.compilador.exceptions.SyntaxException;
import br.unicap.compilador.lexico.*;

public class Parser {
	private Scanner scanner; // analisador léxico
	private Token token;   // o token atual

	/* o Parser recebe o analisador léxico como parâmetro no construtor
	 * pois a cada procedimento invoca-o sob demanda
	 */
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public void programa() {
		token = scanner.nextToken();
		if(token.getText().compareTo("int") != 0) {
			throw new SyntaxException("int Expected!");
		}
		token = scanner.nextToken();
		if(token.getText().compareTo("main") != 0) {
			throw new SyntaxException("main Expected!");
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
			throw new SyntaxException("Abre parentese Expected!");
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
			throw new SyntaxException("Fecha parenteses Expected!");
		}
		token = scanner.nextToken();
		bloco();
	}
	
	public void bloco() {
		if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
			throw new SyntaxException("Abre chave Expected!");
		}
		do {
			decl_var();
			comando();
			
		} while(token.getType() != Token.TK_CARACTER_especial_fecha_chave);
	}
	
	public void decl_var() {
		tipo();
		id();
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_pontovirgula) {
			throw new SyntaxException("Ponto e virgula Expected!");
		}
	}
	
	public void tipo() {
		token = scanner.nextToken();
		if(token.getText().compareTo("int") != 0 && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0) {
			throw new SyntaxException("Type Expected!");
		}
	}
	
	public void id() {
		token = scanner.nextToken();
		if(token.getType() != Token.TK_IDENTIFIER) {
			throw new SyntaxException("Identifier Expected!");
		}
	}
	
	public void comando() {
		token = scanner.nextToken();
		if(token.getText().compareTo("while") == 0) {
			iteracao();
		}
		else if(token.getText().compareTo("if") == 0) {
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
				throw new SyntaxException("Abre parentese Expected!");
			}
			expr_relacional();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
				throw new SyntaxException("Fecha parenteses Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
				throw new SyntaxException("Abre chave Expected!");
			}
			comando();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
				throw new SyntaxException("Fecha chave Expected!");
			}
			token = scanner.nextToken();
			if(token.getText().compareTo("else") != 0) {
				throw new SyntaxException("Command else Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
				throw new SyntaxException("Abre chave Expected!");
			}
			comando();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
				throw new SyntaxException("Fecha chave Expected!");
			}
		}
		else {
			comando_basico();
		}
	}
	
	public void comando_basico() {
		if(token.getType() == Token.TK_CARACTER_especial_abre_chave) {
			bloco();
		}
		else {
			atribuicao();
		}
	}
	
	public void atribuicao() {
		if(token.getType() != Token.TK_IDENTIFIER) {
			throw new SyntaxException("Identifier Expected!");
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_OPERATOR_atribuidor) {
			throw new SyntaxException("Atribuidor Expected!");
		}
		expr_arit();
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_pontovirgula) {
			throw new SyntaxException("Ponto e virgula Expected!");
		}
	}
	
	public void iteracao() {
		if(token.getText().compareTo("while") != 0) {
			throw new SyntaxException("Command while Expected!");
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
			throw new SyntaxException("Abre parentese Expected!");
		}
		token = scanner.nextToken();
		expr_relacional();
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
			throw new SyntaxException("Fecha parenteses Expected!");
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
			throw new SyntaxException("Abre chave Expected!");
		}
		comando();
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
			throw new SyntaxException("Fecha chave Expected!");
		}
	}
	
	public void expr_relacional() {
		expr_arit();
		token = scanner.nextToken();
		if(token.getType() != Token.TK_OPERATOR_aritmetrico_mais && token.getType() != Token.TK_OPERATOR_aritmetrico_menos && token.getType() != Token.TK_OPERATOR_aritmetrico_multiplicacao && token.getType() != Token.TK_OPERATOR_aritmetrico_divisao) {
			throw new SyntaxException("Operador relacional Expected!");
		}
		expr_arit();
	}

	public void E() {
		T();
		El();

	}

	public void El() {
		token = scanner.nextToken();
		System.out.println(token);
		if (token != null) {
			OP();
			T();
			El();
		}
	}

	public void T() {
		token = scanner.nextToken();
		System.out.println(token);
		if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_INTEIRO && token.getType() != Token.TK_FLOAT) {
			throw new SyntaxException("ID or NUMBER or FLOAT Expected!, found "+token.getType()+" ("+token.getText()+")");
		}

	}

	public void OP() {
		if (token.getType() != Token.TK_OPERATOR_aritmetrico_mais && token.getType() != Token.TK_OPERATOR_aritmetrico_menos && token.getType() != Token.TK_OPERATOR_aritmetrico_divisao && token.getType() != Token.TK_OPERATOR_aritmetrico_multiplicacao) {
			throw new SyntaxException("Operator Expected, found "+token.getType()+" ("+token.getText()+")");
		}
	}
}