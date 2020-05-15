include "globals.mzn";
%Definimos Nuestras variables meidante un array
array[0..9] of var 0..9: Letras;
var int: T;
var int: E;
var int: S;
var int: F;
var int: D;
var int: N;
var int: I;
var int: K;
var int: R;
var int: A;
var int: Resultado1;
var int: Resultado2;
var int: Resultado3;
var int: Resultado4;
%Definimos las restricciones para evitar que existan valores repetidos:
constraint forall(i in 0..9)(count(Letras,i)<=1);
%Asignamos los valores para que sea más fácil de leer
constraint T == Letras[0];
constraint E == Letras[1];
constraint S == Letras[2];
constraint F == Letras[3];
constraint D == Letras[4];
constraint I == Letras[5];
constraint N == Letras[6];
constraint K == Letras[7];
constraint R == Letras[8];
constraint A == Letras[9];
%Restricciones para la suma
%Guardamos el resultado para usarlo en sumas posteriores
constraint Resultado1 = (E + E + E);
%Asignamos el resultado a las variables
constraint E = Resultado1 mod 10;
constraint Resultado2 = (T + T + N);
constraint T = (Resultado2 + ( Resultado1 div 10) )mod 10;
constraint Resultado3 = (S + S + I);
constraint F = (Resultado3 + (Resultado2 + ( Resultado1 div 10) ) div 10) mod 10;
constraint A = (Resultado1 + (Resultado3 + (Resultado2 + ( Resultado1 div 10) ) div 10) div 10) mod 10;
constraint Resultado4 = (T + F + D);
constraint R = (Resultado4 + (Resultado1 + (Resultado3 + (Resultado2 + ( Resultado1 div 10) ) div 10) div 10) div 10) mod 10;
constraint K = (Resultado4 + (Resultado1 + (Resultado3 + (Resultado2 + ( Resultado1 div 10) ) div 10) div 10) div 10) div 10;
solve satisfy;