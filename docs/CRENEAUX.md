# Gestions des créneaux

## En tant qu'administrateur je peux voir, créer et modifier des créneaux "standards" qui seront choisis par les clients pour retirer leurs commandes.

Un créneau standard est défini entre deux heures d'une journée de la semaine, chaque créneau possède une capacité de commande. C'est la limite du nombre de commandes qui peuvent être livrées par les employés du drive. Un créneau standard n'est pas lié à une date précise, il se répète indéfiniment chaque semaine.

*Affichage.*

Les créneaux seront regroupés par jour et triés par heure de début.

Sur la liste, il sera facile de voir l'heure de début et de fin de chaque créneau.

Il serait intéressant que la taille des blocs soit proportionnelle à la durée de chaque créneau.

En cliquant sur un créneau existant un formulaire permet de modifier les paramètres de ce créneau.

*Saisie.*

Sur la liste un lien permet d'afficher un formulaire de saisie d'un nouveau créneau.

Je peux choisir le jour de la semaine, l'heure de début (heure et minute), l'heure de fin et la capacité de commande.

Contrôle de saisie : heure début < heure de fin, heure début > 8h00, heure fin < 20h00, capacité > 0

Si deux créneaux se superposent, je peux tout de même les saisir, mais un message me notifiera quels créneaux se chevauchent.

*Modification.*

Le formulaire est identique à celui de saisie, je peux tout modifier, avec les mêmes contrôles.

## En tant qu'administrateur je peux saisir et paramétrer des jours fériés afin qu'un client ne puisse pas commander un retrait ce jour là.

Les jours fériés sont associés à une date et possèdent un nom, exemple : "Noël 2020".

Une liste me présente l'ensemble des jours fériés, je peux en supprimer un ou plusieurs rapidement.

Je peux créer un nouveau jour férié en précisant la date (jour/mois/année) et son nom.

## En tant qu'administrateur je peux définir des créneaux d'ouverture exceptionnelle afin de proposer le retrait des commandes, un jour donné, même si il est férié ou si il ne possède pas de créneaux standards.

Sur le même principe que les créneaux standards, sauf que je saisis la date du créneaux (jour/mois/année).

Une liste affiche l'ensemble des créneaux exceptionnels à venir (date supérieure à aujourd'hui).

Les créneaux sont regroupés par jour et triés par date de début.
Je peux les modifier et en ajouter avec les même règles que les créneaux standards.

## En tant que client, lorsque je valide mon panier, je choisis un créneau disponible parmi les 6 prochains jours afin de planifier mon retrait. 

Avant de transformer le panier en commande, le créneau de retrait doit être choisi.

Afin de faciliter les tests, il sera possible de choisir une date que le système prendra en compte plutôt que la date du jour.

Cette date devra être passée en paramètre de la page de choix du créneau.

Les créneaux d'ouverture exceptionnels sont prioritaires sur les jours fériés et les créneaux standards.

Je ne peux choisir un créneau que si, pour celui-ci, il n'y a pas déjà un nombre de commandes supérieur ou égal à sa capacité.

Je ne peux choisir un créneau que si il démarre dans au moins 1 heure.

Je choisis le créneau et je valide.

Une page me confirme la validation de ma commande et me précise la date et l'heure du retrait.

Cette commande est associée à ce créneau dans la base de données.
