Nume: Vărzaru Vlad-Ionuț 
Grupa: 331CD

Tema 2 APD - Planificarea de task-uri intr-un datacenter

In cadrul acestei teme am avut de implementat componentele de dispatcher si de 
noduri de calcul din sistemul de planificare.
Implementarea se gaseste in clasele MyDispatcher si MyHost.
In clasa MyDispatcher este implementata logica din spatele algoritmilor de planificare.
RoundRobin -> Acest algoritm este implementat asa cum scrie in enuntul temei, pe masura
	      ce vin task-uri se aloca nodului (i + 1) % n, incepand cu id-ul 0.
ShortestQueue -> Pentru acest algoritm mi-am creat metoda hasRunningTask in MyHost care imi verifica
		daca un nod are deja un task care ruleaza, iar daca are mai adun 1 la dimensiunea cozii,
		dupa care selectez nodul cu dimensiunea minima.
SizeIntervalTaskAssignment-> In functie de tipul taskului, short, medium sau long, le atribui nodurilor
			     1, 2, sau 3.
LeastWorkLeft -> Parcurg toate host-urile si il selectez pe cel cu cea mai putina munca ramasa
 Pentru metoda getWorkLeft parcurg coada de taskuri, iau timpul ramas pentru fiecare task dupa care adun si timpul
 ramas din taskul care ruleaza.

In clasa MyHost, in metoda run() extrag task-ul din coada si il execut prin metoda executeTask. In aceasta metoda
parcurg coada de task-uri, iar daca task-ul curent este preemptibil si daca are prioritate mai mica decat cel din 
coada setez preempted pe true si bag in coada task-ul curent. Daca preempted este true, dau break la executeTask 
si verific din nou in metoda run coada si extrag task-ul cu prioritatea cea mai mica. Daca timpul ramas din task 
este 0, apelez task.finish si setez currentTask pe null si flag-ul pentru metoda hasRunningTask pe false.