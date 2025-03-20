package dev.fastball.platform.web.generator;

import dev.fastball.meta.component.ComponentInfo;
import dev.fastball.meta.utils.JsonUtils;
import dev.fastball.platform.core.exception.GenerateException;
import dev.fastball.platform.web.config.WebPlatformConfig;
import dev.fastball.platform.web.model.NodePackage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.fastball.platform.web.WebPlatformConstants.PACKAGE_FILE_NAME;


/**
 * @author Geng Rong
 */
public class PackageJsonGenerator {
    private PackageJsonGenerator() {
    }

    public static void generate(File generatedCodeDir, List<ComponentInfo<?>> componentInfoList, String packageJsonSourcePath, WebPlatformConfig fastballConfig) {
        try (InputStream inputStream = PackageJsonGenerator.class.getResourceAsStream(packageJsonSourcePath)) {
            NodePackage nodePackage = JsonUtils.fromJson(inputStream, NodePackage.class);
            Map<String, String> materialPackageMap = new HashMap<>();
            for (ComponentInfo<?> componentInfo : componentInfoList) {
                if (!materialPackageMap.containsKey(componentInfo.material().getNpmPackage()) || materialPackageMap.get(componentInfo.material().getNpmPackage()).compareTo(componentInfo.material().getNpmVersion()) < 0) {
                    materialPackageMap.put(componentInfo.material().getNpmPackage(), componentInfo.material().getNpmVersion());
                }
            }
            for (Map.Entry<String, String> dependency : materialPackageMap.entrySet()) {
                nodePackage.getDependencies().put(dependency.getKey(), dependency.getValue());
            }
            if (fastballConfig.getCustomNpmDependencies() != null) {
                for (Map.Entry<String, String> dependency : fastballConfig.getCustomNpmDependencies().entrySet()) {
                    nodePackage.getDependencies().put(dependency.getKey(), dependency.getValue());
                }
            }
            File packageJsonFile = new File(generatedCodeDir, PACKAGE_FILE_NAME);
            FileUtils.write(packageJsonFile, JsonUtils.toPrettyJson(nodePackage), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }
}
