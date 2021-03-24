# SAT

Cebotari Zinaida, 2020

In fiecare clasa se realizeaza citirea datelor de intrare si stocarea lor
in structuri specifice, de asemnea se interpreteaza raspunsul Oracolului si se
redacteaza fisierul de ouput.

Task-ul 1 - pentru a rezolva aceast task, am adaptat datele oferite la 
problema k-coloring. Pentru a reduce aceasta problema la SAT se respecta
3 conditii: 
    * fiecare nod trebuie sa aiba cel putin o culoare;
    * fiecare nod trebuie sa aiba cel mult o culoare;
    * fiecare nod trebuie sa aiba o culoare diferita de culoarea nodurilor
      adiacente;
Eu am folosit doar doua din ele, pentru ca o familie poate avea mai mult de un 
spion.


Fiecare familie este reprezentata in formula booleana de un numar de 
variabile egal cu numarul de spioni. Cand o variabila a unei familii e setata
pe unu, inseamna ca spionul cu numarul egal cu pozitia variabilei in sirul 
de variabile ale familiei respective, este infiltrat in aceasta familie.

Am tinut cont de conditia ca o familie trebuie sa fie infiltrata de cel
putin un spion. Astfel am creat cate o clauza pentru fiecare familie cu 
un numar de variabile egal cu numarul de spioni. Astfel pentru fiecare 
familie cel putin o varibila trebuie sa aiba valoarea 1.

De asemnea am tinut cont de conditia urmatoare: doua familii ce prietenesc
nu pot fi infiltrate de acelasi spion, astfel am creat clauze cu perechi de
variabile negate pentru toate variabile ce reprezinta doua familii 
care prietenesc.

Task-ul 2 - pentru a realiza aceasta reducere SAT am adaptat datele
la k-Clique Problem. Aceasta problema are ca scop gasirea unui subgraf 
complet(clica) de dimensiune k in graf.

Pentru aceasta am tinut cont de trei conditii: prima spune ca fiecare
familie va fi reprezentata de un numar de variabile egal cu dimensiunea
subgrafului cautat. A doua spune ca doar o variabila pentru fiecare familie
poate avea valoarea True. Astfel am creat clause cu perechi de variabile
negate pentru toate variabile asociate fiecarei familii. A treia conditie
spune ca daca doua familii nu prietenesc, atunci se neaga toate combinatiile
de perechi de variabile asociate acestor familii.

Task-ul 3 - in acest task se creiaza un fisier de input pentru task-ul 2.
Pentru familiile care nu prietenesc se considera ca prietenesc si invers. Acest
fisier se transmite la task-ul 2 pentru a se gasi cea mai mare clica. Astfel se
cauta subgrafuri cu dimensiunea maxima, egala cu numarul de familii, pana la 1.
Tinand cont ca clica cautata e formata doar din familii ce nu prietenesc, cand
aceasta o sa fie identificata, familiile ramase o sa trebuiasca sa fie 
arestate, pentru ca sa ramana doar familii ce nu prietenesc.

Task 4 -  se reduce problema Max-SAT partial. Clauzele soft sunt pentru fiecare
familie in parte.Clauzele hard sunt pentru familiile ce prietenesc. 
Se creiaza clauze in care variabilele ce reprezinta doua familii 
ce prietensc sunt negate.
