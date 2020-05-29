(define (domain ej3)

(:types
	nodo - localizacion
	VCE - unidades
	CentroDeMando Barracones Extractor - edificio
	Minerales Gas - recursos
)

(:predicates
	(casillaOcupada ?n - localizacion)
	(tieneRecurso ?n - localizacion ?r -  recursos)
	(edificioEn ?n - localizacion ?e - edificio)
	(unidadEn ?n - localizacion ?u - unidades)
	(seTieneRecurso ?r - recursos)
	(camino ?n1 - localizacion ?n2 - localizacion)
	(ocupada ?u - unidades)
	(necesitaRecurso ?r - recursos ?e - edificio)
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
	:parameters (?u - unidades ?r - recursos ?n1 - localizacion)
	:precondition(AND
		(not(ocupada ?u))
		(unidadEn ?n1 ?u)
		(tieneRecurso ?n1 ?r)
	)
	:effect(AND
		(ocupada ?u)
	)
)

(:action EXTRAERMINERAL
	:parameters(?u - unidades ?r - Minerales ?n - localizacion)
	:precondition(AND
		(ocupada ?u)
		(unidadEn ?n ?u)
		(tieneRecurso ?n ?r)
	)
	:effect(AND
		(seTieneRecurso ?r)
	)
)

(:action EXTRAERGAS
	:parameters(?u - unidades ?r - Gas ?n - localizacion)
	:precondition(AND
		(ocupada ?u)
		(unidadEn ?n ?u)
		(tieneRecurso ?n ?r)
		(exists (?e - Extractor)
			(edificioEn ?n ?e)
		)
	)
	:effect(AND
		(seTieneRecurso ?r)
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
	:parameters (?n - localizacion ?e - Extractor ?u - unidades)
	:precondition(AND
		(not(ocupada ?u))
		(forall (?r - recursos)
			(imply (necesitaRecurso ?r ?e) (seTieneRecurso ?r))
		)
		(unidadEn ?n ?u)
	)
	:effect(AND
		(EdificioEn ?n ?e)
	)
)
)
