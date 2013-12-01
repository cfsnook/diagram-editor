diagram-editor
==============

Take a look at http://www.youtube.com/watch?v=6fvcBUR9vQg

Make sure you have
org.eventb.emf.core
org.eventb.emf.persistence

And Graphiti and (for now) Graphiti Examples in the target platform. From ECLIPSE INDIGO. (Not Kepler)

Then it should 'just work'. Note that it only uses a hardcoded project name right now - it lives in line 104 of EventBDiagramFeatureProvider.java

Design:

EventB Diagram Editor Structure


Graphiti Gives us:
- Diagram
- DiagramTypeProvider
- FeatureProviders

EventB gives us:
- Persistence Layer

We define FeatureProviders which interact with a Project Object.

The project object is reached using a ResourceSet

ResourceSet is told how to construct RodinResource objects,and is then asked to provide one for a project URI.

We provide a translation from project URI to Rodin Serialisation, which acts as a middle man and allows us to work with Rodin despite its incorrect URI scheme.

Once we have a ProjectResource, we create an updater.

The updater goes through the project and makes sure that the items on the diagram match what exists in our domain. It also gets notified of changes, so if something changes in the underlying model, we get a notification and can clear the diagram.

When we change things on the diagram, Graphiti handles the graphics for us (with some exceptions) but we provide model changes by instructing the model (From ProjectResource and its children) to delete, rename etc etc.

Rodin serialises beneath all this.

Extensibility

In GMF world extensibility is really really really difficult. This is because all the code is generated, so we would have to replace and modify the diagram editor at runtime.

Graphiti has no generated code.

At runtime, if we don’t know how to create an object, but it is an EventBNamedElement (maybe just an EventBElement, haven’t decided yet) we look in the extensions table to see if /that/ can tell us how to create it. If it can then we use that. If not we simply treat it naively as an EventBNamedElement and limit our available operations to what would be possible on one of those.
