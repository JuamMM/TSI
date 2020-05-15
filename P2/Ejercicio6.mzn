include "globals.mzn";
%Cada posicion representa el gusto y el valor almacenado la casa
%1-Vasco 2-Catalan 3-Gallego 4-Navarro 5-Andaluz
array[1..5] of var 1..5: Region;
%1-Pintor 2-escultor 3-Diplomático 4-Violinista 5-medico
array[1..5] of var 1..5: Profesion;
%1-Perro 2-caracoles 3-zorro 4-caballo 5-cebra
array[1..5] of var 1..5: Mascotas;
%1-te 2-leche 3-Zumo 4-café 5-Agua
array[1..5] of var 1..5: Bebidas;
%1-Roja 2-Verde 3-Blanca 4-Amarilla 5-Azul
array[1..5] of var 1..5: Colores;
%Restricciones
constraint alldifferent(Colores);
constraint alldifferent(Bebidas);
constraint alldifferent(Mascotas);
constraint alldifferent(Profesion);
constraint alldifferent(Region);
constraint Colores[1]==Region[1];
constraint Mascotas[1]==Region[2];
constraint Profesion[1]==Region[3];
constraint Bebidas[1]==Region[4];
constraint Region[5]==1;
constraint Colores[2]==Bebidas[4];
constraint Colores[2]==Colores[3]+1;
constraint Profesion[2]==Mascotas[2];
constraint Profesion[3]==Colores[4];
constraint Bebidas[2]==3;
constraint Colores[5]==2;
constraint Profesion[4]==Bebidas[3];
constraint Mascotas[3] == Profesion[5]-1 \/ Mascotas[3] == Profesion[5]+1;
constraint Mascotas[4] == Profesion[3]-1 \/ Mascotas[4] == Profesion[3]+1;

solve satisfy;


