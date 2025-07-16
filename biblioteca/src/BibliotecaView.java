import model.*;
import service.BibliotecaService;
import exceptions.*;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class BibliotecaView {
    private final BibliotecaService service = new BibliotecaService();
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        boolean sair = false;
        while (!sair) {
            System.out.println("""
                \n=== Biblioteca ===
                1 - Cadastrar Livro
                2 - Cadastrar Revista
                3 - Listar todos os itens
                4 - Emprestar item
                5 - Devolver item
                6 - Buscar Livro por ISBN
                7 - Buscar Revista por edição
                8 - Buscar por termo
                9 - Sair
                Escolha uma opção:
                """);
            String opcao = scanner.nextLine();

            try {
                switch (opcao) {
                    case "1" -> cadastrarLivro();
                    case "2" -> cadastrarRevista();
                    case "3" -> listarTodos();
                    case "4" -> emprestarItem();
                    case "5" -> devolverItem();
                    case "6" -> buscarPorISBN();
                    case "7" -> buscarPorEdicao();
                    case "8" -> buscarPorTermo();
                    case "9" -> sair = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Encerrando sistema...");
    }

    private void cadastrarLivro() {
        System.out.print("Título do livro: ");
        String titulo = scanner.nextLine();
        System.out.print("Gênero do livro: ");
        String generoCategoria = scanner.nextLine();
        System.out.print("Estilo do livro: ");
        String estiloCategoria = scanner.nextLine();
        System.out.print("Tema do livro: ");
        String temaCategoria = scanner.nextLine();
        System.out.print("Nome do autor: ");
        String nomeAutor = scanner.nextLine();
        System.out.print("Nacionalidade do autor: ");
        String nacionalidade = scanner.nextLine();
        System.out.print("Idade do autor: ");
        int idade = Integer.parseInt(scanner.nextLine());
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        Autor autor = new Autor(nomeAutor, nacionalidade, idade);
        Categoria categoria = new Categoria(generoCategoria, estiloCategoria, temaCategoria);
        Livro livro = new Livro(titulo, autor, categoria, isbn);
        service.cadastrar(livro);
        System.out.println("Livro cadastrado com sucesso!");
    }

    private void cadastrarRevista() {
        System.out.print("Título da revista: ");
        String titulo = scanner.nextLine();
        System.out.print("Gênero da revista: ");
        String generoCategoria = scanner.nextLine();
        System.out.print("Estilo da revista: ");
        String estiloCategoria = scanner.nextLine();
        System.out.print("Tema da revista: ");
        String temaCategoria = scanner.nextLine();
        System.out.print("Nome do autor: ");
        String nomeAutor = scanner.nextLine();
        System.out.print("Nacionalidade do autor: ");
        String nacionalidade = scanner.nextLine();
        System.out.print("Idade do autor: ");
        int idade = Integer.parseInt(scanner.nextLine());
        System.out.print("Número da edição: ");
        int edicao = Integer.parseInt(scanner.nextLine());

        Autor autor = new Autor(nomeAutor, nacionalidade, idade);
        Categoria categoria = new Categoria(generoCategoria, estiloCategoria, temaCategoria);
        Revista revista = new Revista(titulo, autor, categoria, edicao);
        service.cadastrar(revista);
        System.out.println("Revista cadastrada com sucesso!");
    }

    private void listarTodos() {
        List<Item> itens = service.listarTodos();
        if (itens.isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
        } else {
            itens.forEach(i -> System.out.println(i + "\n-----------------"));
        }
    }

    private void emprestarItem() throws Exception {
        System.out.print("É livro ou revista? (L/R): ");
        String tipo = scanner.nextLine().trim().toUpperCase();
        Emprestavel item = null;

        if ("L".equals(tipo)) {
            System.out.print("Digite o ISBN do livro: ");
            String isbn = scanner.nextLine();
            Livro livro = service.buscarPorISBN(isbn);
            item = livro;
        } else if ("R".equals(tipo)) {
            System.out.print("Digite o número da edição da revista: ");
            int edicao = Integer.parseInt(scanner.nextLine());
            List<Revista> revistas = service.buscarPorEdicao(edicao);
            if (revistas.isEmpty()) {
                throw new ItemNaoEncontradoException("Revista não encontrada para essa edição.");
            }
            item = revistas.get(0);
        } else {
            System.out.println("Tipo inválido.");
            return;
        }

        service.emprestar(item);
        System.out.println("Item emprestado com sucesso.");
    }


    private void devolverItem() {
        System.out.print("É livro ou revista? (L/R): ");
        String tipo = scanner.nextLine().trim().toUpperCase();
        Emprestavel item = null;

        try {
            if ("L".equals(tipo)) {
                System.out.print("Digite o ISBN do livro: ");
                String isbn = scanner.nextLine();
                Livro livro = service.buscarPorISBN(isbn);
                item = livro;
            } else if ("R".equals(tipo)) {
                System.out.print("Digite o número da edição da revista: ");
                int edicao = Integer.parseInt(scanner.nextLine());
                List<Revista> revistas = service.buscarPorEdicao(edicao);
                if (revistas.isEmpty()) {
                    System.out.println("Revista não encontrada para essa edição.");
                    return;
                }
                item = revistas.get(0);
            } else {
                System.out.println("Tipo inválido.");
                return;
            }

            service.devolver(item);
            System.out.println("Item devolvido com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

//
//    private Emprestavel buscarEmprestavelPorTitulo(String titulo) throws ItemNaoEncontradoException {
//        return service.buscarPorTermo(titulo).stream()
//                .filter(i -> i instanceof Emprestavel)
//                .map(i -> (Emprestavel) i)
//                .findFirst()
//                .orElseThrow(() -> new ItemNaoEncontradoException("Item não encontrado ou não é emprestável"));
//    }

    private void buscarPorISBN() {
        System.out.print("Digite o ISBN do livro: ");
        String isbn = scanner.nextLine();
        try {
            Livro livro = service.buscarPorISBN(isbn);
            System.out.println(livro);
        } catch (ItemNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscarPorEdicao() {
        System.out.print("Digite o número da edição da revista: ");
        int edicao = Integer.parseInt(scanner.nextLine());
        List<Revista> revistas = service.buscarPorEdicao(edicao);
        if (revistas.isEmpty()) {
            System.out.println("Nenhuma revista encontrada para essa edição.");
        } else {
            revistas.forEach(System.out::println);
        }
    }

    private void buscarPorTermo() {
        System.out.print("Digite termo para busca: ");
        String termo = scanner.nextLine();
        List<Item> itens = service.buscarPorTermo(termo);
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado com esse termo.");
        } else {
            itens.forEach(System.out::println);
        }
    }
}
