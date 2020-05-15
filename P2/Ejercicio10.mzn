include "globals.mzn";
array[0..11] of var 0..1: mochila;
array[0..11] of var int: pesos;
array[0..11] of var int: preferencia;
var int: valoracion;
var int: peso;

constraint pesos[0] = 9;
constraint pesos[1] = 13;
constraint pesos[2] = 153;
constraint pesos[3] = 50;
constraint pesos[4] = 15;
constraint pesos[5] = 68;
constraint pesos[6] = 27;
constraint pesos[7] = 39;
constraint pesos[8] = 23;
constraint pesos[9] = 52;
constraint pesos[10] = 11;
constraint pesos[11] = 32;
constraint preferencia[0] = 150;
constraint preferencia[1] = 35;
constraint preferencia[2] = 200;
constraint preferencia[3] = 160;
constraint preferencia[4] = 60;
constraint preferencia[5] = 45;
constraint preferencia[6] = 60;
constraint preferencia[7] = 40;
constraint preferencia[8] = 30;
constraint preferencia[9] = 10;
constraint preferencia[10] = 70;
constraint preferencia[11] = 30;
%Realizamos la suma  de los pesos.
constraint peso = sum(i in 0..11 where mochila[i]==1)(pesos[i]);
constraint peso <=275;
%Realizamo la suma de la valoracion
constraint valoracion = sum(i in 0..11 where mochila[i]==1)(preferencia[i]);
solve maximize valoracion;
