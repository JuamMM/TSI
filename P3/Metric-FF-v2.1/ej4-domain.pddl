(define (domain ej4)

(:types
	nodo - localizacion
	VCE Marine Segador - unidades
	CentroDeMando Barracones - edificio
	Minerales Gas - recursos
)

(:predicates
	(casillaOcupada ?n - localizacion)
	(tieneRecurso ?n - localizacion ?r -  recursos)
	(edificioEn ?n - localizacion ?e - edificio)
	(unidadEn ?n - localizacion ?u - unidades)
	(extractorEn ?n)
	(seTieneRecurso ?r - recursos)
	(camino ?n1 - localizacion ?n2 - localizacion)
	(ocupada ?u - unidades)
	(necesitaRecurso ?r - recursos ?e - edificio)
	(recursoReclutar ?r - recursos ?u - unidades)
)

(:action NAVEGAR
	:parameters (?u - unidades ?n1 - localizacion ?n2 - localizacion)
	:precondition(AND
		(not(ocupada ?u))
		(unidadEn ?n1 ?u)
		(camino ?n1 ?n2)
	)
	:effect (AND
		(unidadEn ?n2 ?u)
		(not(unidadEn ?n1 ?u))
	)
)

(:action ASIGNAR
	:parameters (?u - VCE ?r - recursos ?n1 - localizacion)
	:precondition(AND
		(not(ocupada ?u))
		(unidadEn ?n1 ?u)
		(tieneRecurso ?n1 ?r)
		(extractorEn ?n1)
	)
	:effect(AND
		(ocupada ?u)
		(seTieneRecurso ?r)
	)
)

(:action RECLUTAR
	:parameters (?u - unidades ?n - localizacion)
	:precondition(AND
		(forall (?l - localizacion)
			(not (unidadEn ?l ?u))
		)
		(exists(?b - CentroDeMando)
			(edificioEn ?n ?b)
		)
		(forall (?r - recursos)
			(imply (recursoReclutar ?r ?u) (seTieneRecurso ?r))
		)
	)
	:effect(
		unidadEn ?n ?u
	)
)

(:action CONSTRUIR
	:parameters (?e - edificio ?n - localizacion ?u - unidades)
	:precondition(AND
		(not(casillaOcupada ?n))
		(not(ocupada ?u))
		(unidadEn ?n ?u)
		(forall (?r - recursos)
			(imply (necesitaRecurso ?r ?e) (seTieneRecurso ?r))
		)
	)
	:effect(AND
		(edificioEn ?n ?e)
		(casillaOcupada ?n)
	)

)

(:action CONSTRUIREXTRACTOR
	:parameters (?n - localizacion ?u - unidades)
	:precondition(AND
		(not(extractorEn ?n))
		(not(ocupada ?u))
		(unidadEn ?n ?u)
	)
	:effect(AND
		(extractorEn ?n)
		(casillaOcupada ?n)
	)
)
)
