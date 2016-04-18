(deftemplate product
    (slot id)
    (slot name)
    (slot type)
    (slot price))

(deftemplate order
    (slot product-id)
    (slot quantity ))

(defrule create-order-for-product
    (product (id ?id) (type ?t&veg))
    =>
    (assert (order (product-id ?id) (quantity 1))))


(deftemplate sensorInfo
    (slot sensorId)
    (slot temperature))


(deftemplate clean-up
    (slot sensorId))


(deftemplate modify-temperature
    (slot sensorId)
    (slot temperature)
    (slot needsHeating
        (type SYMBOL)
        (default false))
    (slot needsCooling
        (type SYMBOL)
        (default false))
    )

(defrule set-is-heating
    ?s <- (sensorInfo (sensorId ?sensorId) (temperature ?temperature&:( or
                (< ?temperature 10 )
                (> ?temperature 30) )) )
    =>
    (if (< ?temperature 10) then
        (assert (modify-temperature (sensorId ?sensorId) (temperature ?temperature) (needsHeating true)))
       else
        (assert (modify-temperature (sensorId ?sensorId) (temperature ?temperature) (needsCooling true)))

    )
	(retract ?s))


(defquery get-temperatures
	"Queries the temperature modifications"
	(modify-temperature))

(defquery get-temperatures-for-sensor
	"comment"
	(declare (variables ?sensorId))
    (modify-temperature (sensorId ?sensorId)))

(defrule cleanup-for-sensor
    (clean-up (sensorId ?id))
    ?t <- (modify-temperature (sensorId ?id))
     =>
     (retract ?t))

(defrule remove-cleanup
    ?c <- (clean-up (sensorId ?id))
    (not (modify-temperature (sensorId ?id)))
    =>
    (retract ?c))

