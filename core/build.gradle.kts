import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    id("kotlin-parcelize")
}



// Adicione isso para o KSP não se perder nas ViewModels
ksp {
    arg("hilt.enableAggregatingTask", "true")
}

android {
    namespace = "com.example.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // 1. Defina o arquivo
        val file = project.rootProject.file("local.properties")

        // 2. Crie a variável do número
        var numero = ""

        // 3. Carregue as propriedades (Sem usar java.util aqui dentro, use apenas Properties)
        if (file.exists()) {
            val props = Properties()
            file.reader().use { props.load(it) }
            numero = props.getProperty("WHATSAPP_NUMERO") ?: ""
        }

        // 4. Gere o campo
        buildConfigField("String", "WHATSAPP_SUPORTE", "\"$numero\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Firebase (Necessário para o Repositório no core)
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Hilt (Necessário para Injeção no core)
    implementation("com.google.dagger:hilt-android:2.55")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    ksp ("com.github.bumptech.glide:ksp:4.16.0")
}