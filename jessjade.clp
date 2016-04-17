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

(reset)
(run)
