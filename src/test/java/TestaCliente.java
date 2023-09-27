import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TestaCliente {


    String enderecoApiCliente = "http://localhost:8080/";
    String endpointCliente = "cliente";
    String endpointDelete = "cliente/apagaTodos";
    String listaVazia = "{}";


    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar, deve apresentar uma lista vazia")
    public void QuandoPegarTodosClientesSemCadastrar_DeveApresentarListaVazia () {
        deletaTodosClientes ();


        given ()
                .contentType(ContentType.JSON)
        .when()
                .get(enderecoApiCliente)
        .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(listaVazia));




    }

    @Test
    @DisplayName("Quando enviar os dados do cliente, o mesmo deve ser mostrado na lista")
    public void cadastraCliente (){

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 30,\n" +
                "  \"nome\": \"Marcelo\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaCadastro = "{\"1\":{\"nome\":\"Marcelo\",\"idade\":30,\"id\":1,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(enderecoApiCliente+endpointCliente)
        .then()
                .statusCode(201)
                .assertThat().body(containsString(respostaCadastro));

    }

    @Test
    @DisplayName("Quando atualizar o cadastro, deve retornar os dados mais recentes")
    public void atualizaCliente() {

    String atualizaCadastro = "{\n" +
            "  \"id\": 1,\n" +
            "  \"idade\": 32,\n" +
            "  \"nome\": \"Marcelo\",\n" +
            "  \"risco\": 0\n" +
            "}";
    String respostaAtualizacao = "{\"1\":{\"nome\":\"Marcelo\",\"idade\":32,\"id\":1,\"risco\":0}}";

    given()
            .contentType(ContentType.JSON)
            .body(atualizaCadastro)
    .when()
            .put(enderecoApiCliente+endpointCliente)
    .then()
            .statusCode(200)
            .assertThat().body(containsString(respostaAtualizacao));

    }

    @Test
    @DisplayName("Quando solicitar a exclusão de um cliente, ele deve ser removido da lista")
    public void deletaCliente () {

        String respostaExclusaoIndividual = "CLIENTE REMOVIDO: { NOME: Vinny, IDADE: 32, ID: 3333 }";

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(enderecoApiCliente+endpointCliente+"/3333")
        .then()
                .statusCode(200)
                .assertThat().body(containsString(respostaExclusaoIndividual));

    }

    //metodo de apoio
    @Test
    @DisplayName("Quando solicitar a exclusão de todos os clientes, deverá retornar que todos os clientes foram removidos")
    public void deletaTodosClientes () {

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(enderecoApiCliente+endpointDelete)
        .then()
                .statusCode(200)
                .assertThat().body(containsString(listaVazia));


    }
}