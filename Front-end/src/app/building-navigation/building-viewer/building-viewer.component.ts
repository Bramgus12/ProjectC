import { Component, OnInit, ViewChild, ElementRef, OnDestroy, AfterViewInit } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { GLTFLoader, GLTF } from 'three/examples/jsm/loaders/GLTFLoader';
import { ThreeUtils } from './three-utils'
import { DevGui } from './dev-gui';

@Component({
  selector: 'app-building-viewer',
  templateUrl: './building-viewer.component.html',
  styleUrls: ['./building-viewer.component.scss']
})
export class BuildingViewerComponent implements AfterViewInit, OnDestroy {
  @ViewChild('threejscontainer') threejsContainer: ElementRef;

  private scene: THREE.Scene;
  private camera: THREE.PerspectiveCamera;
  private renderer: THREE.WebGLRenderer;
  private orbitControls: OrbitControls;
  private nextFrameId: number;

  private devGui: DevGui;

  constructor() {
    // Renderer
    this.renderer = new THREE.WebGLRenderer({
      antialias: true,
      logarithmicDepthBuffer: false,
    });
    this.renderer.gammaOutput = true;
    this.renderer.setSize(1280, 720);
    this.renderer.setPixelRatio( window.devicePixelRatio );
    console.log(this.renderer);
    
    // Camera
    this.camera = new THREE.PerspectiveCamera(50, 1280/720, 2.0, 200);
    this.camera.position.set(46, 30, -15);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.orbitControls.target.set(22, 2, -34);
    this.orbitControls.update();
    
    // Scene
    this.scene = new THREE.Scene();

    // Lighting
    const ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0xffffff, 1.0);
    this.scene.add(ambientLight);

    // Load models
    const loadingManager: THREE.LoadingManager = new THREE.LoadingManager();
    loadingManager.onProgress = (url, itemsLoaded, itemsTotal) => {
      // console.log('Loading file: ' + url + '.\nLoaded ' + itemsLoaded + ' of ' + itemsTotal + ' files.');
    };
    loadingManager.onLoad = () => {
      console.log('Loading complete!');
    }
    loadingManager.onError = (url) => {
      // console.log('There was an error loading ' + url);
    };

    const gltfLoader = new GLTFLoader(loadingManager);
    gltfLoader.load(
      'assets/building-viewer/Wijnhaven.glb',
      (gltf: GLTF) => {
        const floorMeshes: THREE.Mesh[] = ThreeUtils.getMeshesByBuildingPart(gltf.scene, "Floor");
        floorMeshes.forEach(floorMesh => {

          const material = <THREE.MeshStandardMaterial> floorMesh.material;
          material.color.set(0xffffff);
          material.aoMapIntensity = 0.7;

          if(floorMesh.userData.floorNumber != 5){
            floorMesh.visible = false;
          }
          
        });

        const wallMeshes: THREE.Mesh[] = ThreeUtils.getMeshesByBuildingPart(gltf.scene, "Wall");
        wallMeshes.forEach(wallMesh => {

          const material = <THREE.MeshStandardMaterial> wallMesh.material;
          material.color.set(0xffffff);
          material.aoMapIntensity = 0.7;
          
          if(wallMesh.userData.floorNumber != 5){
            wallMesh.visible = false;
          }
          
        });

        console.log(wallMeshes);

        this.scene.add(gltf.scene);
      },
    );

    // Dev gui for monitoring three.js performance
    // This should be removed or commented out once the 3D viewer correctly.
    this.devGui = new DevGui();

  }

  ngAfterViewInit() {
    // Add three.js canvas element to the div in this component.
    this.threejsContainer.nativeElement.appendChild(this.renderer.domElement);
    this.devGui.appendToElement(this.threejsContainer.nativeElement);
    this.resizeCanvasToContainer();
    this.animate();
  }

  ngOnDestroy() {
    // Remove event listeners from orbit controls
    this.orbitControls.dispose();
    // Stop render loop
    window.cancelAnimationFrame(this.nextFrameId);
    this.scene = undefined;
  }  

  // Main render loop for 3D view.
  animate() {
    // Start monitoring frame
    this.devGui.stats.begin();

    // Render scene
    this.renderer.render(this.scene, this.camera);

    // End monitoring frame
    this.devGui.stats.end();

    this.nextFrameId = window.requestAnimationFrame(() => this.animate());
  }

  onResize() {
    this.resizeCanvasToContainer();
  }

  resizeCanvasToContainer() {
    const width: number = this.threejsContainer.nativeElement.clientWidth;
    const height: number = this.threejsContainer.nativeElement.clientHeight;
    if (this.renderer.domElement.width != width || this.renderer.domElement.height != height) {
      this.renderer.setSize(width, height);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
    }
  }

}