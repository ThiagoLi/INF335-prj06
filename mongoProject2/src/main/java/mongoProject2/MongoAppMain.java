package mongoProject2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class MongoAppMain {

	public static void imprimeProdutos(MongoCollection<Document> collection)
	{
	System.out.println ("\nImprimindo Produtos");
	Iterable<Document> produtos = collection.find();
	for (Document produto : produtos) {
		String nome = produto.getString("nome");
		String descricao = produto.getString("descricao");
		String valor = produto.getString("valor");
		String estado = produto.getString("estado");
		System.out.println(nome + "--" + descricao + "--" + valor + "--" + estado);
	}
	}
	
	public static void insereProduto(String nome, String descricao, String valor, String estado, MongoCollection<Document> collection)
	{
		Document document = new Document ();
		document.append("nome", nome);
		document.append("descricao", descricao);
		document.append("valor", valor);
		document.append("estado", estado);
		
		collection.insertOne(document);
	}
	
	public static void alteraValor(String nome, String valor, MongoCollection<Document> collection)
	{
		Document novoValor = new Document();
		novoValor.append("$set", new Document("valor", valor));
		
		Document filtroProduto = new Document ();
		filtroProduto.append("nome", nome);
		collection.updateOne(filtroProduto, novoValor);
	}
	
	public static void apagaProduto(String nome, MongoCollection<Document> collection)
	{
		Document filtroProduto = new Document ();
		filtroProduto.append("nome", nome);
		collection.deleteOne(filtroProduto);
	}
	

	public static void main(String[] args) {
	
		System.out.println("Conectando com o mongo db...");
		
		MongoClient client = MongoClients.create("mongodb://localhost");
		
		System.out.println("Conectando com a base test...");
		
		MongoDatabase db = client.getDatabase("test");
		
		System.out.println("Lista colecoes da minha base de dados");
		Iterable<Document> collections = db.listCollections();
		for (Document col : collections) {
			System.out.println(col.get("name"));
		}
		
		MongoCollection<Document> collection = db.getCollection("produtos");
		MongoAppMain.imprimeProdutos(collection);
		

		String produtoNome = "Novo Produto";
		String produtoDescricao = "2 T";
		String produtoValor = "20000";
		String produtoEstado = "Muito Novo";
		
		
		MongoAppMain.insereProduto(produtoNome, produtoDescricao, produtoValor, produtoEstado, collection);
		
		System.out.println("\nLista com Item Adicionado");
		MongoAppMain.imprimeProdutos(collection);
		
		String novoValor = "3000";
		
		
		MongoAppMain.alteraValor(produtoNome, novoValor, collection);
		
		System.out.println("\nValor alterado");
		MongoAppMain.imprimeProdutos(collection);
		
		MongoAppMain.apagaProduto(produtoNome, collection);
		
		System.out.println("\nLista com o Item Apagado");
		MongoAppMain.imprimeProdutos(collection);
		
		System.out.println("\nConexao encerrada");
		client.close();
		
	}
		
}
