include "globals.mzn";
array[0..9] of var 0..9: Digitos;
%De esta forma nos aseguramos que el número de 0 en el array se corresponde con el valor almacenado en la primera componente del vector
constraint forall(i in 0..9)(count(Digitos,0)==Digitos[0]);
%Repetimos la operación anterior para todas las componentes
constraint forall(i in 0..9)(count(Digitos,1)==Digitos[1]);
constraint forall(i in 0..9)(count(Digitos,2)==Digitos[2]);
constraint forall(i in 0..9)(count(Digitos,3)==Digitos[3]);
constraint forall(i in 0..9)(count(Digitos,4)==Digitos[4]);
constraint forall(i in 0..9)(count(Digitos,5)==Digitos[5]);
constraint forall(i in 0..9)(count(Digitos,6)==Digitos[6]);
constraint forall(i in 0..9)(count(Digitos,7)==Digitos[7]);
constraint forall(i in 0..9)(count(Digitos,8)==Digitos[8]);
constraint forall(i in 0..9)(count(Digitos,9)==Digitos[9]);
solve satisfy;
