(define (domain ej6)
(:requirements :fluents)

(:types
	nodo - localizacion
	VCE Marine Segador - unidades
	CentroDeMando Barracones BahiaIngenieria Deposito Extractor - edificio
	Minerales Gas - recursos
	invVce invSeg invMar - investigacion
)

(:predicates
	(casillaOcupada ?n - localizacion)
	(tieneRecurso ?n - localizacion ?r -  recursos)
	(edificioEn ?n - localizacion ?e - edificio)
	(unidadEn ?n - localizacion ?u - unidades)
	(camino ?n1 - localizacion ?n2 - localizacion)
	(ocupada ?u - unidades)
	(necesitaEdificio ?e - edificio ?u - unidades)
	(necesitaInvestigacion ?u - unidades ?i - investigacion)
	(seTieneInves ?i - investigacion)
)

(:functions
	(limiteAlmacenamiento)
	(cantidadRecurso ?r - recursos)
	(necesitaRecurso ?r - recursos ?e - edificio)
	(investigacionRecurso ?r - recursos ?i - investigacion)
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

(:action DESASIGNAR
	:parameters (?u - VCE)
	:precondition(AND
		(ocupada ?u)
	)
	:effect(AND
		(not(ocupada ?u))
	)
)

(:action EXTRAERMINERAL
	:parameters (?u - VCE ?r - Minerales ?n - localizacion)
	:precondition(AND
		(unidadEn ?n ?u)
		(tieneRecurso ?n ?r)
		(<= (cantidadRecurso ?r) (limiteAlmacenamiento))
	)
	:effect(AND
		(increase (cantidadRecurso ?r) 10)
	)
)

(:action EXTRAERGAS
	:parameters (?u - VCE ?r - Gas ?n - localizacion)
	:precondition(AND
		(unidadEn ?n ?u)
		(exists (?e - Extractor)
			(edificioEn ?n ?e)
		)
		(tieneRecurso ?n ?r)
	)
	:effect(AND
		(increase (cantidadRecurso ?r) 10)
	)
)

(:action INVESTIGAR
	:parameters(?i - investigacion ?b - BahiaIngenieria)
	:precondition(AND
		(not(seTieneInves ?i))
		(exists (?n - localizacion)
			(edificioEn ?n ?b)
		)
		(forall (?r1 - recursos)(AND
			(<= (investigacionRecurso ?r1 ?i) (cantidadRecurso ?r1))
			(<= (investigacionRecurso ?r1 ?i) (limiteAlmacenamiento))
		))

	)
	:effect(AND
		(forall (?r - recursos)
			(decrease (cantidadRecurso ?r) (investigacionRecurso ?r ?i))
		)
		(seTieneInves ?i)
	)
)


(:action RECLUTAR
	:parameters (?u - unidades ?n - localizacion)
	:precondition(AND
		(exists (?b - CentroDeMando)
			(edificioEn ?n ?b)
		)
		(exists (?e - edificio)
			(necesitaEdificio ?e ?u)
		)
		(forall (?i - investigacion)
			(imply (necesitaInvestigacion ?u ?i) (seTieneInves ?i))
		)
		(forall (?l - localizacion)
			(not (unidadEn ?l ?u))
		)
		(forall (?r1 - recursos)(AND
			(<= (recursoReclutar ?r1 ?u) (cantidadRecurso ?r1))
			(<= (recursoReclutar ?r1 ?u) (limiteAlmacenamiento))
		))
	)
	:effect(AND
		(forall (?r - recursos)
			(decrease (cantidadRecurso ?r) (recursoReclutar ?r ?u))
		)
		(unidadEn ?n ?u)
	)
)

(:action CONSTRUIR
	:parameters (?e - edificio ?n - localizacion ?u - unidades)
	:precondition(AND
		(not (exists (?l - localizacion)
			(edificioEn ?l ?e)
		))
		(not(casillaOcupada ?n))
		(not(ocupada ?u))
		(unidadEn ?n ?u)
		(forall (?r1 - recursos)(AND
			(<= (necesitaRecurso ?r1 ?e) (cantidadRecurso ?r1))
			(<= (necesitaRecurso ?r1 ?e) (limiteAlmacenamiento))
		))
	)
	:effect(AND
		(forall (?r - recursos)
			(decrease (cantidadRecurso ?r) (necesitaRecurso ?r ?e))
		)
		(edificioEn ?n ?e)
		(casillaOcupada ?n)
	)
)

(:action CONSTRUIRDEPOSITO
	:parameters (?e - Deposito ?n - localizacion ?u - unidades ?b - CentroDeMando)
	:precondition(AND
		(not(casillaOcupada ?n))
		(not(ocupada ?u))
		(unidadEn ?n ?u)
		(forall (?r1 - recursos)(AND
			(<= (necesitaRecurso ?r1 ?e) (cantidadRecurso ?r1))
			(<= (necesitaRecurso ?r1 ?e) (limiteAlmacenamiento))
		))
	)
	:effect(AND
		(forall (?r - recursos)
			(decrease (cantidadRecurso ?r) (necesitaRecurso ?r ?e))
		)
		(edificioEn ?n ?e)
		(casillaOcupada ?n)
		(increase (limiteAlmacenamiento) 100)
	)
)

(:action CONSTRUIREXTRACTOR
	:parameters (?e - Extractor ?n - localizacion ?u - VCE)
	:precondition(AND
		(not(ocupada ?u))
		(unidadEn ?n ?u)
		(forall (?r1 - recursos)(AND
			(<= (necesitaRecurso ?r1 ?e) (cantidadRecurso ?r1))
			(<= (necesitaRecurso ?r1 ?e) (limiteAlmacenamiento))
		))
	)
	:effect(AND
		(forall (?r - recursos)
			(decrease (cantidadRecurso ?r) (necesitaRecurso ?r ?e))
		)
		(edificioEn ?n ?e)
		(casillaOcupada ?n)
	)
)

)
