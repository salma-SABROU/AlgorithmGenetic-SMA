# Implémentation de l'algorithme génétique en utilisant le système multi-agents

## Algorithme génétique
Un algorithme génétique est une technique d'optimisation inspirée par la théorie de l'évolution de Darwin. Il est utilisé pour résoudre des problèmes d'optimisation, tels que la recherche de la meilleure solution pour un ensemble de contraintes données. Voici un exemple simple d'algorithme génétique :

  1- Initialisation : Générer une population initiale de solutions candidates (appelées individus) de manière aléatoire.

  2- Évaluation : Évaluer chaque individu en fonction de sa qualité en utilisant une fonction d'évaluation.

  3- Sélection : Sélectionner les meilleurs individus pour la reproduction.

  4- Croisement : Appliquer des opérations de croisement pour créer des descendants à partir des parents sélectionnés.

  5- Mutation : Appliquer des opérations de mutation pour modifier légèrement les descendants générés lors de l'étape de croisement.

  6- Remplacement : Remplacer les individus les moins performants de la population par les nouveaux individus générés.

  7- Répéter : Répéter les étapes 2 à 6 jusqu'à ce qu'un critère d'arrêt prédéfini soit atteint (par exemple, un nombre maximum d'itérations ou une valeur      d'évaluation minimale atteinte)

Le processus d'optimisation de l'algorithme génétique est guidé par la fonction d'évaluation, qui mesure la qualité de chaque solution candidate. L'objectif est de maximiser cette fonction en trouvant la meilleure solution possible pour le problème donné.

Les opérations de croisement et de mutation sont des mécanismes clés de l'algorithme génétique, qui permettent de générer de nouvelles solutions candidates en combinant les caractéristiques des individus existants. Ces opérations sont utilisées pour explorer l'espace des solutions possibles et éviter de rester coincé dans un minimum local.

L'algorithme génétique peut être utilisé pour résoudre une variété de problèmes d'optimisation, tels que l'optimisation de fonctions mathématiques, la planification de tournées, la conception de circuits, la sélection de fonctionnalités, etc.
## Système Multi Agent
Un système multi-agent (SMA) est un système composé de plusieurs entités autonomes appelées agents, qui interagissent entre elles pour atteindre des objectifs communs ou individuels. Les agents d'un SMA peuvent être conçus pour avoir des capacités de perception, de raisonnement et d'action, leur permettant de prendre des décisions et d'agir de manière autonome.

Voici quelques caractéristiques et concepts clés liés aux systèmes multi-agents :

  Autonomie des agents : Chaque agent dans un SMA est autonome, ce qui signifie qu'il peut fonctionner indépendamment et prendre des décisions de manière autonome en fonction de son environnement, de ses connaissances et de ses objectifs.

  Interaction entre agents : Les agents d'un SMA peuvent interagir entre eux en échangeant des messages, en coopérant, en négociant ou même en se confrontant. Ces interactions permettent aux agents de partager des informations, de coordonner leurs actions et de résoudre des problèmes de manière collective.

  Hétérogénéité des agents : Les agents dans un SMA peuvent être hétérogènes, c'est-à-dire qu'ils peuvent avoir des capacités, des connaissances ou des perspectives différentes. Cette diversité permet aux agents de collaborer en combinant leurs expertises complémentaires pour résoudre des problèmes complexes.

  Émergence : Les comportements collectifs et les structures complexes peuvent émerger à partir des interactions entre agents individuels. Les SMA permettent l'émergence de phénomènes collectifs qui ne peuvent pas être prédits ou expliqués simplement en étudiant le comportement des agents individuels.

  Adaptation : Les agents d'un SMA peuvent être capables d'apprendre, de s'adapter et de modifier leur comportement en fonction des changements dans leur environnement ou des interactions avec d'autres agents. Cette capacité d'adaptation permet aux SMA de s'ajuster aux nouvelles situations et de résoudre des problèmes dynamiques.

  Applications : Les SMA ont de nombreuses applications dans divers domaines tels que la gestion des transports, la logistique, la planification des ressources, la robotique, les systèmes d'information, les marchés électroniques, etc. Ils offrent une approche décentralisée et flexible pour résoudre des problèmes complexes et distribués.(pour une implementation sequencial voir tp_sma_ga/out/production/tp_sma_ga/ma/enset/ga/sequencial)
### l'architecture centralisée

Dans les systèmes multi-agents (SMA), l'architecture centralisée est une approche dans laquelle un agent centralisé, appelé agent central ou agent médiateur, est responsable de la coordination et de la prise de décision pour l'ensemble du système. Les autres agents du système sont souvent appelés agents subordonnés.
![image](https://github.com/salma-SABROU/AlgorithmGenetique/assets/129564311/42f601b7-5bd9-4608-bac1-13f49baf8951)

## Explication du code 
Notre implémentation est basée sur une architecture centralisée dans laquelle on a le mainAgents c'est l'agents hôte qui gere les differents agents qui échangent des informations (fitness, chromosomes) et effectuent des opérations de sélection et de croisement pour générer une nouvelle génération d'agents.
  - AgentMain :
Déclaration des variables : Le code déclare plusieurs variables, telles que "agentsFitness" (une liste d'objets AgentFitness), "rnd" (un objet Random), qui seront utilisées tout au long du code.

Méthode "setup" : C'est la méthode principale de l'agent qui est exécutée lors de son démarrage. Elle est marquée avec l'annotation @Override, ce qui signifie qu'elle remplace une méthode existante de la classe parente "Agent".

  a. DFAgentDescription et ServiceDescription : Ces classes sont utilisées pour rechercher les descriptions des autres agents dans le système qui offrent le service "ga" (défini dans la variable "serviceDescription"). Les agents trouvés sont ajoutés à la liste "agentsFitness" avec un fitness initial de 0.

  b. calculateFintness() : Cette méthode est appelée pour envoyer une demande aux agents pour calculer leur fitness. Un message est envoyé à chaque agent avec le contenu "fitness" et le type de message "REQUEST".

  c. SequentialBehaviour : Un objet SequentialBehaviour est créé pour regrouper les comportements (sub-behaviours) qui seront exécutés séquentiellement.

  d. Première partie du SequentialBehaviour : Un comportement anonyme est ajouté au SequentialBehaviour. Ce comportement est exécuté de manière cyclique (CyclicBehaviour) jusqu'à ce qu'il atteigne un certain nombre d'itérations (GAUtils.POPULATION_SIZE). Dans chaque itération, l'agent principal reçoit un message (ACLMessage) et met à jour le fitness de l'agent correspondant dans la liste "agentsFitness". Lorsque le nombre d'itérations atteint GAUtils.POPULATION_SIZE, la liste "agentsFitness" est triée par ordre décroissant de fitness et la méthode "showPopulation()" est appelée pour afficher la population d'agents.

  e. Deuxième partie du SequentialBehaviour : Un autre comportement anonyme est ajouté au SequentialBehaviour. Ce comportement est exécuté jusqu'à ce qu'une condition spécifique soit remplie (done() renvoie true). Dans chaque itération, le comportement effectue la sélection, le croisement (crossover) des agents et envoie le chromosome résultant à deux agents sélectionnés. Ensuite, il reçoit les messages de retour contenant le fitness mis à jour pour ces agents.

Méthodes supplémentaires :

  calculateFintness() : Cette méthode envoie une demande à chaque agent pour calculer son fitness. Un message est créé avec le contenu "fitness" et envoyé à chaque agent dans la liste "agentsFitness".

  setAgentFintess() : Cette méthode met à jour le fitness d'un agent spécifié dans la liste "agentsFitness" en fonction de l'agent AID et du fitness fournis.

  showPopulation() : Cette méthode affiche le nom de chaque agent et son fitness à partir de la liste "agentsFitness".

  sendMessage() : Cette méthode envoie un message (ACLMessage) à un agent spécifié avec le contenu et le type de message spécifiés.

  - Agents(Cet agent représente un individu dans un algorithme génétique):
Méthode "setup" : C'est la méthode principale de l'agent qui est exécutée lors de son démarrage. Elle est marquée avec l'annotation @Override, ce qui signifie qu'elle remplace une méthode existante de la classe parente "Agent".

  a. DFAgentDescription et ServiceDescription : Ces classes sont utilisées pour décrire l'agent et spécifier le type de service qu'il fournit ("ga" dans ce cas). L'agent s'enregistre ensuite auprès du DFService pour annoncer qu'il fournit ce service.

  b. Initialisation des gènes : Les gènes de l'individu sont initialisés avec des caractères choisis aléatoirement à partir de la chaîne de caractères "CHARATERS" définie dans la classe "GAUtils".

  c. CyclicBehaviour : Un comportement cyclique (CyclicBehaviour) est ajouté à l'agent pour gérer les messages reçus.

    mutation() : Cette méthode est appelée lorsque l'agent reçoit un message avec le contenu "mutation". Elle effectue une mutation sur les gènes de l'individu avec une certaine probabilité définie dans "MUTATION_PROB" de la classe "GAUtils".

    calculateFintess() : Cette méthode est appelée lorsque l'agent reçoit un message avec le contenu "fitness". Elle calcule la valeur de fitness de l'individu en comparant ses gènes avec la solution cible spécifiée dans "SOLUTION" de la classe "GAUtils". La valeur de fitness est ensuite envoyée en réponse dans un message.

    sendChromosome() : Cette méthode est appelée lorsque l'agent reçoit un message avec le contenu "chromosome". Elle envoie en réponse un message contenant les gènes de l'individu.

    changeChromosome() : Cette méthode est appelée lorsque l'agent reçoit un message avec un contenu autre que "mutation", "fitness" ou "chromosome". Elle met à jour les gènes de l'individu avec les gènes reçus, puis effectue une mutation et recalcule la valeur de fitness.

  Méthode "takeDown" : Cette méthode est appelée lorsque l'agent est terminé. Elle se charge de désenregistrer l'agent du DFService.

## Test de l'algorithme
Comme vous voyez dans cette image, on a trouvé le résultat après 1873 itérations.
![image](https://github.com/salma-SABROU/AlgorithmGenetique/assets/129564311/8ec493c4-a778-4f83-a843-23ff571eeac8)
