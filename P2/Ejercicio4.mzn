include "globals.mzn";
%0-No hay clase 1-IA-G1 2-IAG2 3-TSIG1 4-TSIG2
%5-FBDG1 6-FBDG2 7-TSIG3 8-TSIG4 9-FBDG3 10-FBDG4
%11-IAG3 12-IAG4
array[1..4,9..12] of var 0..12: Aulas;
%En este array almacenamos a que hora empieza un profesor a dar clase, y en que aula 0 implican que no hay clase
array[1..4,9..12] of var 0..4: Profesores;
%Nos aseguramos que los profesores 2 y 4 no empiecen a dar clase a las horas no disponibles.
constraint forall(i in 1..4)(Profesores[i,10] != 2);
constraint forall(i in 1..4)(Profesores[i,9] != 4);
%Nos aseguramos que un profesor no de clase en dos aulas distintas en la misma hora
constraint forall(c,i in 1..4, j in 9..12 where i!=c)(Profesores[c,j]!=Profesores[i,j]); 
%Obligamos a que exita un mínimo de asignaciones por profesor esto va a representar el número de asignaturas por grupo que tiene que dar
constraint count([Profesores[i,j] | i in 1..4, j in 9..12],1)==4;
constraint count([Profesores[i,j] | i in 1..4, j in 9..12],2)==2;
constraint count([Profesores[i,j] | i in 1..4, j in 9..12],3)==4;
constraint count([Profesores[i,j] | i in 1..4, j in 9..12],4)==2;
%Asignamos basándonos en el vector de Prfesores las clases a las aulas
constraint forall(i in 1..4, j in 9..12 where Profesores[i,j]==1)(Aulas[i,j]=1 \/ Aulas[i,j]=2 \/ Aulas[i,j]=3 \/ Aulas[i,j]=4);
constraint forall(i in 1..4, j in 9..12 where Profesores[i,j]==2)(Aulas[i,j]=5 \/ Aulas[i,j]=6);
constraint forall(i in 1..4, j in 9..12 where Profesores[i,j]==3)(Aulas[i,j]=7 \/ Aulas[i,j]=8 \/ Aulas[i,j]=9 \/ Aulas[i,j]=10);
constraint forall(i in 1..4, j in 9..12 where Profesores[i,j]==4)(Aulas[i,j]=11 \/ Aulas[i,j]=12);
%Nos aseguramos de que cada una excepto la 0 aparezcan sólo una vez
constraint forall(c in 1..12)(count([Aulas[i,j] | i in 1..4, j in 9..12],c)==1);
solve satisfy;