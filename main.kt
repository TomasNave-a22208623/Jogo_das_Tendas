import java.io.File

val dataInvalida = "Data invalida"
val menorIdade = "Menor de idade nao pode jogar"


fun criaMenu(): String {
    val menu1 = "\nBem vindo ao jogo das tendas\n\n1 - Novo jogo\n0 - Sair\n"
    return menu1
}

fun validaTamanhoMapa(numLinhas: Int, numColunas: Int): Boolean {    /* tipos de terreno */
    if (numLinhas == 6 && numColunas == 5) {
        return true
    } else if (numLinhas == 6 && numColunas == 6) {
        return true
    } else if (numLinhas == 8 && numColunas == 8) {
        return true
    } else if (numLinhas == 8 && numColunas == 10) {
        return true
    } else if (numLinhas == 10 && numColunas == 8) {
        return true
    } else if (numLinhas == 10 && numColunas == 10) {
        return true
    }
    return false
}

fun validaDataNascimento(data: String?): String? {
    //------------------------------------------Validação dos Espaços--------------------------------------------------//

    if (data == null) {    /*enter*/
        return dataInvalida
    }
    if (data.length != 10) { /*comprimento*/
        return dataInvalida
    }
    if (data[2] != '-') { /*posicao 3 - */
        return dataInvalida
    }
    if (data[5] != '-') { /*posicao 6 - */
        return dataInvalida
        //---------------------------------------Conversão de Strings para inteiros-----------------------------------------//
    } else {   /*dividir a string em dia / mes / ano */
        val diaString = data[0] + data[1].toString()
        val dia = diaString.toInt()
        val anoString = data[6] + data[7].toString() + data[8].toString() + data[9].toString()
        val ano = anoString.toInt()
        val mesString = data[3] + data[4].toString()
        val mes = mesString.toInt()
        //-------------------------------------------Validação Ano------------------------------------------------------//
        if (ano < 1990 || ano > 2022) {
            return dataInvalida
        }
        //-------------------------------------------Validação Mes------------------------------------------------------//
        if (mes > 12 || mes < 1) {
            return dataInvalida
        }
        //-------------------------------------------Validação Mes de Fevereiro-----------------------------------------//
        if (mesString == "02") {
            if ((ano % 4 == 0 && ano % 100 != 0) || ano % 400 == 0) {
                if (dia > 29) {
                    return dataInvalida
                }
            } else if (dia > 28) {
                return dataInvalida
            }
        }
        //-----------------------------Validação meses com 31 dias e meses com 30 dias----------------------------------//
        if (mesString == "04" || mesString == "06" || mesString == "09" || mesString == "11") {
            if (dia > 30) {
                return dataInvalida
            }
        } else if (dia > 31) {
            return dataInvalida
        }
        //---------------------------------Validação se é maior de idade------------------------------------------------//

        if (ano == 2004) {                                     /* a partir de 01-11-2004 é menor de 18 anos.*/
            if (mesString == "11" || mesString == "12") {
                return menorIdade
            }
        }
        if (ano > 2004) {
            return menorIdade
        }
    }
    return null     /* null data valida */
}

fun criaLegendaHorizontal(numColunas: Int): String {
    var nmrColuna = numColunas            /*variavel por parametro nao pode ser alterada*/
    val caracter = 'A'
    var codigoAscii = caracter.code     /* codigo da letra*/
    var legenda = "A"                  /* Para começar no A */
    while (nmrColuna > 1) {
        codigoAscii = (codigoAscii + 1)
        legenda = legenda + " | ${codigoAscii.toChar()}"
        nmrColuna--
    }
    return legenda
}


fun processaCoordenadas(coordenadasStr: String?, numLines: Int, numColumns: Int): Pair<Int, Int>? {
    if (coordenadasStr == null) {
        return null
    }

    if (coordenadasStr.length >= 3) {   /*tamanho da String das coordenadas */
        if (coordenadasStr[1] == ',' && coordenadasStr.length == 3) {     /* coordenadas das tabelas com 1 algarismo */
            val linhaString = coordenadasStr[0].toString()   /*linha sozinha string */
            val linha = linhaString.toInt()                  /*linha sozinha Int */
            val coluna = coordenadasStr[2]                   /* coluna sozinha */
            val colunaAscii = coluna.code
            val letraColuna = 'A'
            val letraAscii = letraColuna.code
            val colunas = letraAscii + (numColumns - 1)      /* ultima coluna em codigo */
            val coordLinhas = linha - 1                      /* coordenada linha para colocar no pair */
            val coordColunas = colunaAscii - letraAscii      /* codigo da letra inserida - codigo do A = coord colu */
            val pares = Pair(coordLinhas, coordColunas)      /*exp: B -> B - A = 66 - 65 = 1 ou seja coluna 1 no array*/


            if (linha <= numLines) { /*valida linha*/
                if (colunaAscii <= colunas && colunaAscii >= letraAscii) {  /* Tem que estar entre o A e a ultima coluna*/
                    return pares /* aceita a coordenada */
                } else {
                    return null /* se for menor que 65 ou maior que a ultima coluna */
                }
            } else {
                return null
            }
        } else if (coordenadasStr[2] == ',' && coordenadasStr.length == 4) {   /* coordenadas das tabelas com 2 algarismos */
            val linhaString = coordenadasStr[0].toString() + coordenadasStr[1].toString() /* somar em string */
            val linha = linhaString.toInt()
            val coluna = coordenadasStr[3]
            val colunaAscii = coluna.code
            val letraColuna = 'A'
            val letraAscii = letraColuna.code
            val colunas = letraAscii + (numColumns - 1)
            val coordLinhas = linha - 1
            val coordColunas = colunaAscii - letraAscii
            val pares = Pair(coordLinhas, coordColunas)


            if (linha <= numLines) {
                if (colunaAscii <= colunas && colunaAscii >= letraAscii) {
                    return pares
                } else {
                    return null
                }
            } else {
                return null
            }

        } else {
            return null
        }

    } else {
        return null
    }
}

fun leContadoresDoFicheiro(numLines: Int, numColumns: Int, verticais: Boolean): Array<Int?> {
    val linha: String
    val partes: Array<String?>
    val linhas = File("${numLines}x${numColumns}.txt").readLines()     /* ler o ficheiro*/

    if (verticais == true) {   /*ler contador vertical */
        val contadores: Array<Int?> = arrayOfNulls(numColumns)
        linha = linhas[0]
        partes = linha.split(",").toTypedArray()    /* cria um array com cada numero separado */
        for (i in 0..numColumns - 1) {
            contadores[i] = partes[i]?.toIntOrNull() /*coloca os numeros do array do ficheiro no array de nulls*/
            if (contadores[i] == 0) {
                contadores[i] = null              /* mete no array null em vez de 0 */
            }
        }
        return contadores

    } else { /*ler contador horizontal */
        val contadores: Array<Int?> = arrayOfNulls(numLines)
        linha = linhas[1]
        partes = linha.split(",").toTypedArray()
        for (i in 0..numLines - 1) {
            contadores[i] = partes[i]?.toIntOrNull()
            if (contadores[i] == 0) {
                contadores[i] = null
            }
        }
        return contadores   /* retorna a legenda num array separado */
    }
}

fun criaLegendaContadoresHorizontal(contadoresVerticais: Array<Int?>): String { /*criar primeira linha (nome da funcao errada)*/

    val contador = contadoresVerticais
    var legenda: String = ""
    if (contador[0] != null && contador[0] != 0) {      /* Quando a primeira posição não é 0 */
        legenda = "$legenda" + "${contador[0]} "
    } else {
        legenda = "$legenda  "          /* Quando a primeira posição é 0 */
    }

    for (i in 1 until contador.size - 1) { /*contador.size = 6 -> contador[6] está fora do array */
        if (contador[i] != 0 && contador[i] != null) {      /* Quando a posição do meio não é 0 */
            legenda = "$legenda " + " ${contador[i]} "
        } else {
            legenda = "$legenda    "             /* Quando a posição do meio é 0 */
        }
    }
    if (contador[contador.size - 1] != null && contador[contador.size - 1] != 0) {   /* Quando a posição do fim é difrente de 0 */
        legenda = "$legenda  " + "${contador[contador.size - 1]}"
    }
    return legenda
}

fun leTerrenoDoFicheiro(numLines: Int, numColumns: Int): Array<Array<String?>> {
    val posicaoArvore = File("${numLines}x${numColumns}.txt").readLines()    /*ler o ficheiro*/
    val terreno = Array(numLines) { Array<String?>(numColumns) { null } }   /*array onde todos os espaços sao null*/
    var numero = 2
    var linha: Int?
    var coluna: Int?
    var partes: Array<String?>

    do {
        partes = posicaoArvore[numero].split(",").toTypedArray()  /* crio um array com as coordenadas separadas */
        linha = partes[0]?.toIntOrNull()
        coluna = partes[1]?.toIntOrNull()
        for (i in 0..numLines - 1) {           /* percorrer o array toda */
            for (j in 0..numColumns - 1) {
                if (linha == i && coluna == j) {   /* se as coordenadas forem iguais as do ficheiro colocar arvore */
                    terreno[i][j] = "A"
                }
            }
        }
        numero++
    } while (numero < posicaoArvore.size)   /* faz do 2 até á ultima linha doo ficheiro */
    return terreno
}

fun criaTerreno(
        terreno: Array<Array<String?>>, contadoresVerticais: Array<Int?>?, contadoresHorizontais: Array<Int?>?,
        mostraLegendaHorizontal: Boolean = true, mostraLegendaVertical: Boolean = true
): String {
    var tabuleiro: String = ""
    var legendaContadoresHorizontal: String = ""
    val numColunas = terreno[0].size
    val numLinhas = terreno.size
    var numeroColunas = terreno[0].size
    val legendaHorizontal = criaLegendaHorizontal(terreno[0].size) /* cria uma legenda para o numero de colunas */
    if (contadoresVerticais != null) {
        legendaContadoresHorizontal = criaLegendaContadoresHorizontal(contadoresVerticais)  /*faz a legenda contador */
    }
    if (contadoresVerticais != null) {
        tabuleiro = tabuleiro + "       $legendaContadoresHorizontal"   /* cria primeira linha (contador das tendas)*/
    }
    if (mostraLegendaHorizontal) {
        if (contadoresVerticais != null) {
            tabuleiro = tabuleiro + "\n"     /* passa para a segunda linha */
        }
        tabuleiro = tabuleiro + "     | $legendaHorizontal"  /* cria a segunda linha (legenda A | B )*/
    }
    for (i in 0 until numLinhas) {
        if (tabuleiro != "") {
            tabuleiro = tabuleiro + "\n"  /* passa para a terceira linha */
        }
        if (contadoresHorizontais != null) {
            if (contadoresHorizontais[i] == 0 || contadoresHorizontais[i] == null) { /* se o primeiro espaco do array  for vazio*/
                tabuleiro = tabuleiro + "  "                                         /* ou seja se nessa linha nao existe tenda para ser metida*/
            } else {
                tabuleiro = tabuleiro + contadoresHorizontais[i] + " "
            }
        } else {
            tabuleiro = tabuleiro + "  "       /* se nao ouver contador horizontal*/
        }
        if (mostraLegendaVertical) {
            if (i >= 9) {
                tabuleiro = tabuleiro + "${i + 1} " /* quando é para fazer o 10 (2 caracteres)*/
            } else {
                tabuleiro = tabuleiro + " ${i + 1} "  /* quando é para fazer o resto */
            }
        } else {
            tabuleiro = tabuleiro + "   "
        }
        for (j in 0 until numColunas) {
            if (j == numColunas - 1) {     /* ultima coluna */
                if (terreno[i][j] == "A") {
                    tabuleiro = tabuleiro + "| \u25B3" /*passar A para tenda */
                } else if (terreno[i][j] == "T") {
                    tabuleiro = tabuleiro + "| T"
                } else {
                    tabuleiro = tabuleiro + "|  "
                }
            } else if (terreno[i][j] == "A") {    /* outras todas */
                tabuleiro = tabuleiro + "| \u25B3 "
            } else if (terreno[i][j] == "T") {
                tabuleiro = tabuleiro + "| T "      /*$$tem que ter mais espaço no fim da letra se for nas colunas do meio*/
            } else {
                tabuleiro = tabuleiro + "|   "
            }
        }
    }
    tabuleiro = tabuleiro + "\n"  /* passa para a terceira linha */
    while (numColunas > 1) {
            tabuleiro = tabuleiro + "----"
            numeroColunas--
        }

    return tabuleiro
}

fun temArvoreAdjacente(terreno: Array<Array<String?>>, coords: Pair<Int, Int>): Boolean { /* se a coord esta adjacente a alguma arvore */
    val linha = coords.first
    val coluna = coords.second
    if (linha != terreno.size - 1) {    /*se nao estiver na ultima linha (coord)*/
    } else if (coluna != terreno[0].size - 1) { /* se nao estiver na ultima coluna */
        if (terreno[linha][coluna + 1] == "A") {  /* coluna a frente */
            return true
        }
    } else if (coluna != 0) { /*se nao estiver na primeira coluna*/
        if (terreno[linha][coluna - 1] == "A") {  /* coluna a tras */
            return true
        }
    }
    return false
}

fun temTendaAdjacente(terreno: Array<Array<String?>>, coords: Pair<Int, Int>): Boolean { /* se a coord esta adjacente a alguma tenda */
    val linha = coords.first
    val coluna = coords.second
    if (linha != 0) {                                                          /* se nao estiver na 1 linha */
        if (terreno[linha - 1][coluna] == "T") {   /* em cima */
            return true
        }
        if (coluna != 0) {   /* e se nao estiver na 1 coluna */
            if (terreno[linha - 1][coluna - 1] == "T") {    /* diagonal */
                return true
            } else if (terreno[linha][coluna - 1] == "T") {  /* antes */
                return true
            }
        }
        if (coluna != terreno[0].size - 1) {  /* e se nao estiver na ultima coluna */
            if (terreno[linha - 1][coluna + 1] == "T") {  /* diagonal */
                return true
            } else if (terreno[linha][coluna + 1] == "T") {  /* depois */
                return true
            }
        }
    } else if (linha != terreno.size - 1) {                                   /* se nao estiver na ultima linha */
        if (terreno[linha + 1][coluna] == "T") {   /* depois */
            return true
        }
        if (coluna != 0) {  /* e se nao estiver na 1 coluna */
            if (terreno[linha + 1][coluna - 1] == "T") {  /* diagonal */
                return true
            } else if (terreno[linha][coluna - 1] == "T") { /* antes */
                return true
            }
        }
        if (coluna != terreno[0].size - 1) {  /* e se nao estiver na ultima coluna */
            if (terreno[linha + 1][coluna + 1] == "T") {  /* diagonal */
                return true
            } else if (terreno[linha][coluna + 1] == "T") {  /* depois */
                return true
            }
        }
    }
    return false
}

fun contaTendasColuna(terreno: Array<Array<String?>>, coluna: Int): Int {  /* numero de tendas na coluna pedida */
    var numeroTendas = 0

    if (coluna >= 0 && coluna <= terreno[0].size - 1) {
        for (i in 0..terreno.size - 1) {
            if (terreno[i][coluna] == "T") {
                numeroTendas++
            }
        }
        return numeroTendas
    } else {
        return numeroTendas
    }
}

fun contaTendasLinha(terreno: Array<Array<String?>>, linha: Int): Int {  /*numero de tendas na linha pedida */
    var numeroArvores = 0

    if (linha >= 0 && linha <= terreno.size - 1) {
        for (j in 0..terreno[0].size - 1) {
            if (terreno[linha][j] == "A") {
                numeroArvores++
            }
        }
        return numeroArvores
    } else {
        return numeroArvores
    }
}

fun colocaTenda(terreno: Array<Array<String?>>, coords: Pair<Int, Int>): Boolean { /* verificar se a jogada é valida e colocar a tenda*/
    val coordsLinha = coords.first
    val coordsColuna = coords.second
    if (terreno[coordsLinha][coordsColuna] == null) {   /*se o espaço estiver vazio */
        if (temArvoreAdjacente(terreno, coords)) {     /*e se esta adjacente a uma arvore*/
            if (temTendaAdjacente(terreno, coords)) {  /*se tem tenda adjacente (nao pode)*/
                return false
            } else {                                  /*se não tem tenda adjacente*/
                terreno[coordsLinha][coordsColuna] = "T"  /*coloca tenda */
                return true
            }
        } else {
            return false
        }
    } else if (terreno[coordsLinha][coordsColuna] == "T") { /* se ja tem uma tenda tirar*/
        terreno[coordsLinha][coordsColuna] = null
        return true
    } else {
        return false
    }
}

fun terminouJogo(    /* termina se todas as tendas forem colocadas (uma por arvore ) e bater certo com os contadores*/
                     terreno: Array<Array<String?>>,
                     contadoresVerticais: Array<Int?>,
                     contadoresHorizontais: Array<Int?>
): Boolean {
    var numArvores = 0
    var numTendas = 0
    val numTendasLinha = Array<Int>(terreno.size) { 0 }
    val numTendasColuna = Array<Int>(terreno[0].size) { 0 }
    for (i in 0..terreno.size - 1) {        /*percorrer cada quadricula do terreno*/
        for (j in 0..terreno[0].size - 1) {
            if (terreno[i][j] == "A") {
                numArvores++                /*contador arvores */
            }
            if (terreno[i][j] == "T") {
                numTendas++                 /*contador tendas */
                numTendasLinha[i]++         /*somar o numero de tendas que tem por linha em cada espaço do array*/
                numTendasColuna[j]++        /*somar o numero de tendas que tem por coluna em cada espaço do array*/
            }
        }
    }
    for (i in 0..terreno.size - 1) {
        for (j in 0..terreno[0].size - 1) {
            if (contadoresHorizontais[i] == null) {   /* nao pode haver tendas nestas linhas*/
                contadoresHorizontais[i] = 0
            }
            if (contadoresVerticais[j] == null) {
                contadoresVerticais[j] = 0            /* nao pode haver tendas nestas coluna*/
            }
        }
    }


    if (numArvores == numTendas) {
        if (numTendasLinha.contentEquals(contadoresHorizontais)) {    /*se o array do ficheiro for igual ao array criado nesta função*/
            if (numTendasColuna.contentEquals(contadoresVerticais)) {
                return true
            }
        }
    }
    return false
}

fun main() {
    var colunas: Int? = 0
    var linhas: Int? = 0
    var terreno: Array<Array<String?>>
    var contadoresVert: Array<Int?>
    var contadoresHorz: Array<Int?>
//-----------------------(Inicio)-----------[Ciclo] Geral-------------------------------------------------------------//
    do {
        var terrenoInvalido = 0
        var menorDeIdade = 0
        var coordenasInvalidas = 0
//----------------------------------------------Menu Jogo-------------------------------------------------------------//
        println(criaMenu())
        val resposta = readLine()?.toIntOrNull()
        if (resposta == null || resposta != 1 && resposta != 0) {
            println("Opcao invalida")
        } else if (resposta == 0) {
            println("")
        } else if (resposta == 1) {
//-------------------------------[Ciclo] Pergunta = Quantas Colunas?-------------------------------------------------------//
            do {
                println("Quantas linhas?")
                linhas = readLine()?.toIntOrNull()
                if (linhas == null || linhas <= 0) {
                    println("Resposta invalida")
                }
            } while (linhas == null || linhas <= 0)
//--------------------------------[Ciclo] Pergunta = Quantas Linhas?----------------------------------------------------//
            do {
                println("Quantas colunas?")
                colunas = readLine()?.toIntOrNull()
                if (colunas == null || colunas <= 0) {
                    println("Resposta invalida")
                }
            } while (colunas == null || colunas <= 0)
//------------------------------------Validação Tamanho do Mapa------------------------------------------------------//
            if (validaTamanhoMapa(linhas, colunas) != true) {
                terrenoInvalido = 1
                println("Terreno invalido")
            }
//--------------------------------[Ciclo] Validação Maior de Idade---------------------------------------------------//
            if (linhas == 10 && colunas == 10) {
                do {
                    println("Qual a sua data de nascimento? (dd-mm-yyyy)")
                    val data = readLine()
                    if (validaDataNascimento(data) == "Menor de idade nao pode jogar") {
                        println(validaDataNascimento(data))
                        menorDeIdade = 1
                    } else if (validaDataNascimento(data) == dataInvalida) {
                        println(validaDataNascimento(data))
                    }
                } while (validaDataNascimento(data) == dataInvalida)
            }
//---------------------------------------------Cria Terreno----------------------------------------------------------//
            if (terrenoInvalido == 0 && menorDeIdade == 0) {
                terreno = leTerrenoDoFicheiro(linhas, colunas) /*le ficheiro e cria o terreno em array*/
                contadoresVert = leContadoresDoFicheiro(linhas, colunas, true) /* cria contadorVer em array*/
                contadoresHorz = leContadoresDoFicheiro(linhas, colunas, false)/* cria contadorHor em array*/
                println(criaTerreno(terreno, contadoresVert, contadoresHorz))/*Escreve o terreno*/
//------------------------------[Ciclo] Pergunta = Coordenadas da tenda?--------------------------------------------/
                do {
                    println("Coordenadas da tenda? (ex: 1,B)")
                    val coordenadas = readLine()
                    val coordenadasPair: Pair<Int, Int>? = processaCoordenadas(coordenadas, linhas, colunas)
                    if (coordenadasPair == null) {
                        println("Coordenadas invalidas")
                    } else {
                        coordenasInvalidas = 1
                        colocaTenda(terreno, coordenadasPair)
                        if (colocaTenda(terreno, coordenadasPair) == false) {
                            println("Tenda nao pode ser colocada nestas coordenadas")
                        }
                    }
                } while (processaCoordenadas(coordenadas, linhas, colunas) == null)
            }
        }
    } while (resposta != 1 && resposta != 0 || terrenoInvalido == 1 || menorDeIdade == 1 || coordenasInvalidas == 1)
//----------------------(Fim)---------------Ciclo Geral--------------------------------------------------------------//
}


