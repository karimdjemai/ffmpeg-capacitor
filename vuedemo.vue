<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
	<q-page class="flex justify-center q-pa-md index-page">
		<q-file class="full-width" outlined v-model="video" @input="handleInput()">
			<template v-slot:prepend>
				<q-icon name="attach_file" />
			</template>
		</q-file>

		<video ref="video" autoplay controls height="200px" />
	</q-page>
</template>

<script>
	import { Plugins, FilesystemDirectory, FilesystemEncoding } from '@capacitor/core';
	const { Filesystem } = Plugins
	const { FFMPEG } = Plugins

	export default {
        name: 'PageIndex',
        data() {
            return {
                video: null,
	            uri: ''
            }
        },

        methods: {
	        handleInput() {

		        try {
		            let reader = new FileReader();
		            reader.readAsDataURL(this.video);
		            reader.onload = async () => {
			            //console.log(reader.result.split(',')[1])

			            const result = await Filesystem.writeFile({
				            path: 'video.webm',
				            data: reader.result.split(',')[1],
				            directory: FilesystemDirectory.Cache,
			            })
			            console.log('Wrote file', result)

			            //let mediaInformation = await FFMPEG.getMediaInformation({ path: result.uri })

			            let outURI = result.uri.slice(0, result.uri.lastIndexOf('/') + 1) + 'out.webm'

			            let command = '-i ' +
			                          result.uri +
			                          ' -t 5 -c:v vp8 -y ' +
			                          outURI

			            console.log(command)

			            //let pStat = FFMPEG.enableStatisticsCallback()
			            let pExec = FFMPEG.execute({command})


			            await Promise.all([pExec])//, pStat])

			            // const file = await Filesystem.readFile({
				        //     path: 'out.webm',
				        //     directory: FilesystemDirectory.Cache
			            // })
			            //
			            // console.log('read file', file)
			            //
			            // this.$refs.video.src = 'data:video/webm;base64,' + file.data
		            }
		            reader.onerror = function (error) {
			            console.log('Error: ', error)
		            };

	            } catch(e) {
		            console.error('Unable to write file', e);
	            }
            }
        }
  }
</script>
<style lang="scss">
    .index-page {
        h4 {
            margin: 0;
        }
    }
</style>
