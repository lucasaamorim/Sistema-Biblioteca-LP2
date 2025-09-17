#!/bin/bash

javac -cp "gson-2.11.0.jar:." -d out src/br/universidade/biblio/Biblioteca.java \
             src/br/universidade/biblio/Emprestimo.java \
             src/br/universidade/biblio/GerenciadorDeDados.java \
             src/br/universidade/biblio/Livro.java \
             src/br/universidade/biblio/Main.java \
             src/br/universidade/biblio/Relatorio.java \
             src/br/universidade/biblio/Usuario.java \
             src/br/universidade/biblio/Utils.java


