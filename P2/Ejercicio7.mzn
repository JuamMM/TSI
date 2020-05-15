include "globals.mzn";
%1-A,2-B,3-C,4-D,5-E,6-F,7-G,8-H,9-I
%Array que recoge las tareas y en que orden se realizan. Se almacena la prioridad, si para la
%posición 1 se almacena un 1, quiere decir que la tarea A se realiza la primera.
array[1..9] of var 1..9: Orden;
%Tiempo necesario para cada Tarea
array[1..9] of var int: Tiempo;
array[1..9] of var 0..10: maxtiempos;
constraint Tiempo[1] = 7;
constraint Tiempo[2] = 3;
constraint Tiempo[3] = 1;
constraint Tiempo[4] = 8;
constraint Tiempo[5] = 2;
constraint Tiempo[6] = 1;
constraint Tiempo[7] = 1;
constraint Tiempo[8] = 3;
constraint Tiempo[9] = 2;
var int: maximoTareas;
var int: tiempoTotal;
%Aseguramos lor órdenes de las tareas.
constraint Orden[2]>Orden[1];
constraint Orden[3]>Orden[2];
constraint Orden[4]>Orden[1];
constraint Orden[5]>Orden[3] /\ Orden[5]>Orden[4];
constraint Orden[6]>Orden[3] /\ Orden[6]>Orden[4];
constraint Orden[7]>Orden[3] /\ Orden[7]>Orden[4];
constraint Orden[8]>Orden[1];
constraint Orden[9]>Orden[6] /\ Orden[9]>Orden[8];
constraint maximoTareas = max(Orden);
constraint forall(i in 0..maximoTareas, j,e in 0..9 where i==j /\ e!=j /\ Orden[j]==Orden[e])(maxtiempos[i] = max(Tiempo[e],Tiempo[j]));
constraint forall(i in 0..maximoTareas, j,e in 0..9 where i==j /\ Orden[j]!=Orden[e])(maxtiempos[i] = Tiempo[j]);
constraint tiempoTotal = sum(maxtiempos);
solve minimize tiempoTotal;