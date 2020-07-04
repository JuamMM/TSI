(define (problem ej2)

	(:domain EJ2)

	(:objects
		c11 c12 c13 c14 c15
		c21 c22 c23 c24 c25
		c31 c32 c33 c34 c35
		c41 c42 c43 c44 c45
		c51 c52 c53 c54 c55 - nodo
		base - CentroDeMando
		b1 - Barracones
		v1 v2 v3 - VCE
		ex1 - Extractor
		m1 - Minerales
		g1 - Gas
	)

	(:INIT
		;caminos
		(camino c11 c12)
		(camino c11 c21)

		(camino c12 c11)
		(camino c12 c22)
		(camino c12 c13)

		(camino c13 c12)
		(camino c13 c23)
		(camino c13 c14)

		(camino c14 c13)
		(camino c14 c24)
		(camino c14 c15)

		(camino c15 c14)
		(camino c15 c25)

		(camino c21 c11)
		(camino c21 c22)
		(camino c21 c31)

		(camino c22 c21)
		(camino c22 c12)
		(camino c22 c32)
		(camino c22 c23)

		(camino c23 c22)
		(camino c23 c13)
		(camino c23 c33)
		(camino c23 c24)

		(camino c24 c23)
		(camino c24 c25)
		(camino c24 c14)
		(camino c24 c34)

		(camino c25 c24)
		(camino c25 c15)
		(camino c25 c35)

		(camino c31 c21)
		(camino c31 c32)
		(camino c31 c41)

		(camino c32 c31)
		(camino c32 c33)
		(camino c32 c22)
		(camino c32 c42)

		(camino c33 c32)
		(camino c33 c34)
		(camino c33 c23)
		(camino c33 c43)

		(camino c34 c33)
		(camino c34 c35)
		(camino c34 c24)
		(camino c34 c44)

		(camino c35 c34)
		(camino c35 c25)
		(camino c35 c45)

		(camino c41 c42)
		(camino c41 c31)
		(camino c41 c51)

		(camino c42 c41)
		(camino c42 c43)
		(camino c42 c32)
		(camino c42 c52)

		(camino c43 c42)
		(camino c43 c44)
		(camino c43 c33)
		(camino c43 c53)

		(camino c44 c43)
		(camino c44 c45)
		(camino c44 c54)
		(camino c44 c34)

		(camino c45 c44)
		(camino c45 c55)
		(camino c45 c35)

		(camino c51 c52)
		(camino c51 c41)

		(camino c52 c51)
		(camino c52 c53)
		(camino c52 c42)

		(camino c53 c52)
		(camino c53 c54)
		(camino c53 c43)

		(camino c54 c55)
		(camino c54 c53)
		(camino c54 c44)

		(camino c55 c45)
		(camino c55 c54)

		;minerales
		(tieneRecurso c11 m1)
		(tieneRecurso c12 m1)
		(tieneRecurso c44 m1)

		;gases
		(tieneRecurso c14 g1)
		(tieneRecurso c21 g1)

		;centro de mando
		(edificioEn c33 base)

		;casillas libres
		(casillaOcupada c11)
		(casillaOcupada c12)
		(casillaOcupada c21)

		(casillaOcupada c14)
		(casillaOcupada c44)

		(casillaOcupada c33)

		;VCEs
		(unidadEn c55 v1)
		(unidadEn c55 v2)
		(unidadEn c55 v3)

		;materiales necesarios para un barracon
		(necesitaRecurso g1 b1)
		(necesitaRecurso m1 ex1)
	)

	(:goal
		(AND
			(edificioEn c22 b1)
		)
	)
)
