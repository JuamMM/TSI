include "globals.mzn";
%En el array almacenamos a que hora empieza cada profesor su clase:
%la posición 0 referencia a P1, 1 a P2, etc..
array[1..6] of var 9..14: Horario;
%No hay dos  clases a la misma hora
constraint forall(i in 9..14)(count(Horario,i)<=1);
%Nos aseguramos que se cumplen las condiciones, siempre permitiendo que se peuda dar
%una hora de clase como indica el problema
constraint Horario[1] <= 14 /\ Horario[1] >= 11;
constraint Horario[2] <= 12 /\ Horario[2] >= 11;
constraint Horario[3] <= 13 /\ Horario[3] >= 10;
constraint Horario[4] <= 12 /\ Horario[4] >= 10;
constraint Horario[5] <= 12 /\ Horario[5] >= 11;
constraint Horario[6] <= 14 /\ Horario[6] >= 9;
solve satisfy;