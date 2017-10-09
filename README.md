Block Chain de AVL

Lookup
Contruir el arbol completo cada vez que nos piden lookup requiere mucho tiempo. Por otro lado almacenar todos los
bloques requeriria mucho espacio. Para solucionarlo se puede crear un objeto historial que tenga un hashmap con los
indices y los arboles almacenados y un metodo que tenga un int como parametro y se eliminen todos aquellos registros
con distancia menor a este numero.

Data del Block
Para alamacenar la operacion vamos a armar un "diccionario" en el que cada operacion corresponda a un numero y eso
facilitara las reiteradas lecturas del dato
Se está guardando la altura en los nodos.

