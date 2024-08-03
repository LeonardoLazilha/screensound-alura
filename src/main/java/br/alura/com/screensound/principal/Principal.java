package br.alura.com.screensound.principal;

import br.alura.com.screensound.model.Artista;
import br.alura.com.screensound.model.Musica;
import br.alura.com.screensound.model.TipoArtista;
import br.alura.com.screensound.repository.ArtistaRepository;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final ArtistaRepository repositorio;

    Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 9) {
            var menu = """
                    \n\n**************************************
                        ♩♫♪ Screen Sound Músicas ♩♫♪
                    **************************************                           
                                        
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                                    
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 9:
                    System.out.println("""
                            \n\n**************************************\n
                                  Encerrando a aplicação!\n
                            **************************************\n\n
                              """);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while (cadastrarNovo.equalsIgnoreCase("s")) {
            System.out.println("Infome o nome do artista: ");
            var nome = leitura.nextLine();

            System.out.println("Qual tipo de artista (SOLO, DUPLA ou BANDA");
            var tipo = leitura.nextLine();
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());

            //criou construtor
            Artista artista = new Artista(nome, tipoArtista);

            //criar repositorio para artista
            repositorio.save(artista);

            System.out.println("Deseja cadastrar um novo artista?");
            cadastrarNovo = leitura.nextLine();
        }
    }

    private void cadastrarMusicas(){
        System.out.println("A música pertence a qual artista? ");
        var nome = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);

        if (artista.isPresent()){
            System.out.println("Qual o nome da música? ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("Artista não encontrado ;(");
        }

    }

    private void listarMusicas(){
        List<Artista> artistas = repositorio.findAll();
       artistas.forEach(System.out::println);
    }

}
