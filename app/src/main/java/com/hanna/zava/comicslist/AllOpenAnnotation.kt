package com.hanna.zava.comicslist

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class AllOpenAnnotation

@AllOpenAnnotation
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting