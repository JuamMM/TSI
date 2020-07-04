(define (domain ej5)

(:types
	nodo - localizacion
	VCE Marine Segador - unidades
	CentroDeMando Barracones BahiaIngenieria Extractor - edificio
	Minerales Gas - recursos
	invVce invSeg invMar - investigacion
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
	(investigacionRecurso ?r - recursos ?i - investigacion)
	(necesitaRecurso ?r - recursos ?e - edificio)
	(necesitaEdificio ?e - edificio ?u - unidades)
	(recursoReclutar ?r - recursos ?u - unidades)
	(necesitaInvestigacion ?u - unidades ?i - investigacion)
	(seTieneInves ?i - investigacion)
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

(:action RECLUTAR
	:parameters (?u - unidades ?n - localizacion)
	:precondition(AND
		(forall (?e - edificio)
			(exists (?l - localizacion)
				(imply (necesitaEdificio ?e ?u) (edificioEn ?l ?e))
			)
		)
		(forall (?i - investigacion)
			(imply (necesitaInvestigacion ?u ?i) (seTieneInves ?i))
		)
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
		(forall (?l - localizacion)
			(not (edificioEn ?l ?e))
		)
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

(:action INVESTIGAR
	:parameters(?i - investigacion ?b - BahiaIngenieria)
	:precondition(AND
		(not(seTieneInves ?i))
		(exists (?n - localizacion)
			(edificioEn ?n ?b)
		)
		(forall (?r - recursos)
			(imply (investigacionRecurso ?r ?i) (seTieneRecurso ?r))
		)
	)
	:effect(AND
		(seTieneInves ?i)
	)
)
)
